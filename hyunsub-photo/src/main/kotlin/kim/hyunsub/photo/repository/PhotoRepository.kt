package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository: JpaRepository<Photo, Int> {
	fun findByAlbumId(albumId: Int, page: Pageable = Pageable.unpaged()): List<Photo>
	fun countByAlbumId(albumId: Int): Int
}
