package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.model.RestApiApparelDetail
import kim.hyunsub.apparel.model.copy
import kim.hyunsub.apparel.model.dto.ApparelUpsertImageParams
import kim.hyunsub.apparel.model.dto.ApparelUpsertParams
import kim.hyunsub.apparel.model.toDto
import kim.hyunsub.apparel.model.toEntity
import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.apparel.repository.generateId
import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.model.ImageConvertParams
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ApparelService(
	private val apparelRepository: ApparelRepository,
	private val apparelImageRepository: ApparelImageRepository,
	private val apparelImageService: ApparelImageService,
	private val fsImageClient: FsImageClient,
) {
	private val log = KotlinLogging.logger { }

	fun create(userId: String, params: ApparelUpsertParams): RestApiApparelDetail {
		log.debug { "[Apparel Create] userId=$userId, params=$params" }

		val apparelId = apparelRepository.generateId()

		val images = params.uploads.map { processUploadedImage(it, userId, apparelId) }
		images.forEach { log.debug { "[Apparel Create] newImage=$it" } }

		val apparel = params.apparel.toEntity(
			id = apparelId,
			userId = userId,
			imageId = images.firstOrNull()?.id,
		)
		log.debug { "[Apparel Create] apparel=$apparel" }

		apparelRepository.save(apparel)
		apparelImageRepository.saveAll(images)

		return RestApiApparelDetail(
			apparel = apparel.toDto(),
			images = images.map { it.toDto(userId) }
		)
	}

	fun update(userId: String, apparelId: String, params: ApparelUpsertParams): RestApiApparelDetail {
		log.debug { "[Apparel Update] userId=$userId, apparelId=$apparelId, params=$params" }

		val old = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Apparel Update] oldApparel=$old" }

		val deleteIds = params.deletes
		if (deleteIds.isNotEmpty()) {
			val deleteImages = apparelImageRepository.findAllById(deleteIds)
			apparelImageService.deleteBulk(userId, deleteImages)
		}

		val newImages = params.uploads.map { processUploadedImage(it, userId, apparelId) }
		newImages.forEach { log.debug { "[Apparel Update] newImage=$it" } }

		val imageId = old.imageId
			.takeIf { it != null && !deleteIds.contains(it) }
			?: newImages.firstOrNull()?.id
		val apparel = old.copy(params.apparel).copy(imageId = imageId)
		log.debug { "[Apparel Update] apparel=$apparel" }

		apparelRepository.save(apparel)
		apparelImageRepository.saveAll(newImages)

		val images = apparelImageRepository.findByApparelId(apparelId)

		return RestApiApparelDetail(
			apparel = apparel.toDto(),
			images = images.map { it.toDto(userId) }
		)
	}

	private fun processUploadedImage(it: ApparelUpsertImageParams, userId: String, apparelId: String): ApparelImage {
		val noncePath = FsPathConverter.noncePath(it.nonce)
		val ext = it.ext.lowercase()
		val imageId = apparelImageRepository.generateId()
		val fileName = "$imageId.$ext"
		val path = ApparelPathConverter.imagePath(userId, apparelId, fileName)

		fsImageClient.convert(
			ImageConvertParams(
				input = noncePath,
				output = path,
				resize = "1024x1024>",
				quality = 60,
			)
		)

		return ApparelImage(
			id = imageId,
			apparelId = apparelId,
			ext = ext,
		)
	}

	fun delete(userId: String, apparelId: String): RestApiApparelDetail {
		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val images = apparelImageRepository.findByApparelId(apparelId)

		apparelImageService.deleteBulk(userId, images)
		apparelRepository.delete(apparel)

		return RestApiApparelDetail(
			apparel = apparel.toDto(),
			images = images.map { it.toDto(userId) }
		)
	}
}
