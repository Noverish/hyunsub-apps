package kim.hyunsub.photo.service

import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class AlbumThumbnailService(
	private val albumMapper: AlbumMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	@Async
	fun deleteAsync(photoId: String) {
		deleteBulkInner(listOf(photoId))
	}

	@Async
	fun deleteBulkAsync(photoIds: List<String>) {
		deleteBulkInner(photoIds)
	}

	private fun deleteBulkInner(photoIds: List<String>) {
		val albums = albumMapper.select(AlbumCondition(thumbnailPhotoIds = photoIds))

		for (album in albums) {
			val page = PageRequest.of(0, 1)
			val condition = AlbumPhotoCondition(albumId = album.id, excludePhotoIds = photoIds, page = page)
			val candidates = albumPhotoMapper.select(condition)

			val newThumbnailId = candidates.firstOrNull()?.photoId
			val newAlbum = album.copy(thumbnailPhotoId = newThumbnailId)

			log.debug { "[Album Thumbnail] New album thumbnail: albumId=${album.id}, ${album.thumbnailPhotoId} => $newThumbnailId" }
			albumMapper.update(newAlbum)
		}
	}
}
