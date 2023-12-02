package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.apparel.repository.generateId
import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.client.removeBulk
import kim.hyunsub.common.fs.model.ImageConvertParams
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class ApparelImageService(
	private val fsClient: FsClient,
	private val apparelImageRepository: ApparelImageRepository,
	private val fsImageClient: FsImageClient,
) {
	fun create(userId: String, apparelId: String, nonces: List<String>): List<ApparelImage> {
		val images = nonces.map { createOne(userId, apparelId, it) }
		apparelImageRepository.saveAll(images)
		return images
	}

	private fun createOne(userId: String, apparelId: String, nonce: String): ApparelImage {
		val noncePath = FsPathConverter.noncePath(nonce)
		val ext = Path(noncePath).extension
		val imageId = apparelImageRepository.generateId()
		val fileName = "$imageId.$ext"
		val filePath = ApparelPathConverter.imagePath(userId, apparelId, fileName)

		fsImageClient.convert(
			ImageConvertParams(
				input = noncePath,
				output = filePath,
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

	fun deleteWithIds(userId: String, imageIds: List<String>) {
		val images = apparelImageRepository.findAllById(imageIds)
		delete(userId, images)
	}

	fun delete(userId: String, images: List<ApparelImage>) {
		if (images.isEmpty()) {
			return
		}

		val filePaths = images.map { ApparelPathConverter.imagePath(userId, it.apparelId, it.fileName) }
		apparelImageRepository.deleteAll(images)
		fsClient.removeBulk(filePaths)
	}
}
