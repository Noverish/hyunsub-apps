package kim.hyunsub.photo.service

import kim.hyunsub.photo.repository.condition.AlbumCondition
import kim.hyunsub.photo.repository.condition.PhotoCondition2
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumThumbnailService(
	private val albumMapper: AlbumMapper,
	private val photoMapper: PhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	fun delete(photoId: String) {
		val albums = albumMapper.select(AlbumCondition(thumbnailPhotoId = photoId))
		for (album in albums) {
			val condition = PhotoCondition2(albumId = album.id, page = PageRequest.of(0, 10))
			val nextThumbnail = photoMapper.selectAlbumPhoto(condition)
				.firstOrNull { it.id != photoId }
				?.id
			val newAlbum = album.copy(thumbnailPhotoId = nextThumbnail)
			log.debug { "[Album Thumbnail] New album thumbnail: $newAlbum" }
			albumMapper.update(newAlbum)
		}
	}
}
