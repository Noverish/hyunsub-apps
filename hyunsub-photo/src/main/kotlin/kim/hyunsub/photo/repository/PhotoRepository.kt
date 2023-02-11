package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PhotoRepository: JpaRepository<Photo, Int> {
	fun findByAlbumIdOrderByDate(albumId: Int, page: Pageable = Pageable.unpaged()): List<Photo>
	@Query("SELECT id FROM Photo WHERE albumId = :albumId ORDER BY date")
	fun findIdByAlbumIdOrderByDate(albumId: Int, page: Pageable = Pageable.unpaged()): List<Int>
	fun findByPath(path: String): Photo?
	fun countByAlbumId(albumId: Int): Int
}
