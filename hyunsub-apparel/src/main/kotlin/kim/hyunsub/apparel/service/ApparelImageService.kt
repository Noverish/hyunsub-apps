package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.ApparelRepository
import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class ApparelImageService(
	private val apiCaller: ApiCaller,
	private val apparelRepository: ApparelRepository,
	private val apparelImageRepository: ApparelImageRepository,
	private val randomGenerator: RandomGenerator,
) {
	companion object : Log

	fun upload(userId: String, apparelId: String, image: MultipartFile): ApparelImage {
		val fileName = image.originalFilename
			?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
		log.debug("[Apparel Image Upload] userId={}, apparelId={}, image={}", userId, apparelId, fileName)

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val fileExt = Path(fileName).extension
		val imageId = ApparelImage.generateId(randomGenerator)
		log.debug("[Apparel Image Upload] imageId={}", imageId)
		val imageName = "$imageId.$fileExt"

		val path = FilePathConverter.getApparelImagePath(userId, apparelId, imageName)
		apiCaller.upload(path, image.bytes)

		val apparelImage = ApparelImage(
			id = imageId,
			apparelId = apparelId,
			ext = fileExt
		)
		apparelImageRepository.save(apparelImage)

		if (apparel.imageId == null) {
			val newApparel = apparel.copy(imageId = imageId)
			apparelRepository.save(newApparel)
		}

		return apparelImage
	}

	fun delete(userId: String, apparelId: String, imageId: String) {
		log.debug("[Apparel Image Delete] userId={}, apparelId={}, imageId={}", userId, apparelId, imageId)

		val apparel = apparelRepository.findByIdAndUserId(apparelId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val apparelImage = apparelImageRepository.findByIdAndApparelId(imageId, apparelId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		if (apparel.imageId == apparelImage.id) {
			val newImageId = apparelImageRepository.findByApparelId(apparelId).firstOrNull()?.id
			val newApparel = apparel.copy(imageId = newImageId)
			apparelRepository.save(newApparel)
		}

		apparelImageRepository.deleteById(imageId)
		deleteFile(userId, apparelImage)
	}

	fun deleteFile(userId: String, image: ApparelImage) {
		log.debug("[Apparel Image File Delete] userId={}, image={}", userId, image)
		val path = FilePathConverter.getApparelImagePath(userId, image.apparelId, image.fileName)
		apiCaller.unlink(path)
	}
}
