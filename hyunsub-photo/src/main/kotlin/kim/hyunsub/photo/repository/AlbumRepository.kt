package kim.hyunsub.photo.repository

import kim.hyunsub.common.random.generateRandomString
import kim.hyunsub.photo.repository.entity.Album
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

interface AlbumRepository : JpaRepository<Album, String> {
	@Query(
        """
			SELECT b FROM AlbumOwner a
			INNER JOIN Album b ON b.id = a.albumId
			WHERE a.userId = :userId
			ORDER BY b.regDt DESC
		"""
	)
	fun findByUserId(userId: String, page: Pageable = Pageable.unpaged()): List<Album>

	@Query(
		"""
			SELECT b FROM AlbumOwner a
			INNER JOIN Album b ON b.id = a.albumId
			WHERE a.albumId = :albumId AND a.userId = :userId
		"""
	)
	fun findByAlbumIdAndUserId(albumId: String, userId: String): Album?

	fun findByThumbnailPhotoId(photoId: String): List<Album>
}

fun AlbumRepository.generateId(): String {
	for (i in 0 until 3) {
		val newId = generateRandomString(8)
		if (this.findByIdOrNull(newId) == null) {
			return newId
		}
	}
	throw RuntimeException("Failed to generate new id")
}
