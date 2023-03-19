package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import org.springframework.data.jpa.repository.JpaRepository

interface AlbumPhotoRepository : JpaRepository<AlbumPhoto, AlbumPhotoId> {
	fun findByAlbumId(albumId: String): List<AlbumPhoto>
}
