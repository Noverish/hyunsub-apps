package kim.hyunsub.photo.service

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.removeBulk
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMetadataMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import kim.hyunsub.photo.util.PhotoPathConverter
import kim.hyunsub.photo.util.isVideo
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class PhotoDeleteService(
	private val fsClient: FsClient,
	private val photoMapper: PhotoMapper,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val photoMetadataMapper: PhotoMetadataMapper,
) {
	private val log = KotlinLogging.logger { }

	@Async
	fun checkAndDeleteAsync(photoIds: List<String>) {
		val countedPhotoIds = photoOwnerMapper.countByPhotoIds(photoIds).map { it.photoId }
		val deletePhotoIds = photoIds.filter { it !in countedPhotoIds }
		log.debug { "[Delete Photo] $deletePhotoIds" }
		if (deletePhotoIds.isEmpty()) {
			return
		}

		val photos = photoMapper.selectByIds(deletePhotoIds)

		val originalPaths = photos.map { PhotoPathConverter.original(it) }
		val thumbnailPaths = photos.map { PhotoPathConverter.thumbnail(it) }
		val videoPaths = photos.filter { isVideo(it.fileName) }.map { PhotoPathConverter.video(it) }

		fsClient.removeBulk(originalPaths)
		fsClient.removeBulk(thumbnailPaths)
		if (videoPaths.isNotEmpty()) {
			fsClient.removeBulk(videoPaths)
		}

		photoMapper.deleteByIds(deletePhotoIds)
		photoMetadataMapper.deleteByPhotoIds(deletePhotoIds)
	}
}
