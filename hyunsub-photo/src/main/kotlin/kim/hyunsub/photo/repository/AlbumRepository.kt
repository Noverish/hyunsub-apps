package kim.hyunsub.photo.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.photo.repository.entity.Album
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumRepository : JpaRepository<Album, String> {
	fun findByThumbnailPhotoId(photoId: String): List<Album>
}

fun AlbumRepository.generateId() = generateId(8)
