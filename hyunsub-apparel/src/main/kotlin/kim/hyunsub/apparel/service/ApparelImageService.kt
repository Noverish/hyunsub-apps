package kim.hyunsub.apparel.service

import kim.hyunsub.apparel.repository.ApparelImageRepository
import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.fs.removeBulk
import org.springframework.stereotype.Service

@Service
class ApparelImageService(
	private val fsClient: FsClient,
	private val apparelImageRepository: ApparelImageRepository,
) {
	fun deleteBulk(userId: String, images: List<ApparelImage>) {
		val deletePaths = images.map { ApparelPathConverter.imagePath(userId, it.apparelId, it.fileName) }
		fsClient.removeBulk(deletePaths)
		apparelImageRepository.deleteAll(images)
	}
}
