package kim.hyunsub.photo.service

import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class AlbumThumbnailService(
	private val albumRepository: AlbumRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	fun delete(photoId: String) {
		val albums = albumRepository.findByThumbnailPhotoId(photoId)
		for (album in albums) {
			val nextThumbnail = albumPhotoRepository.findByAlbumId(album.id).firstOrNull()?.id
			val newAlbum = album.copy(thumbnailPhotoId = nextThumbnail)
			log.debug { "[Album Thumbnail] New album thumbnail: $newAlbum" }
			albumRepository.save(newAlbum)
		}
	}
}
