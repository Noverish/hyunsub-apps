package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

interface PhotoRepository : JpaRepository<Photo, String> {
	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotos(userId: String, page: Pageable = Pageable.unpaged()): List<Photo>

	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId AND a.photoId < :photoId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotosWithNext(userId: String, photoId: String, page: Pageable): List<Photo>

	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId AND a.photoId > :photoId
			ORDER BY a.photoId ASC
		"""
	)
	fun selectMyPhotosWithPrev(userId: String, photoId: String, page: Pageable): List<Photo>

	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN Photo b ON b.id = a.photoId
			WHERE a.userId = :userId AND a.photoId = :photoId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotosWithPhotoId(userId: String, photoId: String): Photo?

	fun findByHash(hash: String): Photo?

	@Modifying
	@Query("UPDATE Photo SET id = :to where id = :from")
	fun updateId(from: String, to: String): Int
}

fun PhotoRepository.generateId(date: OffsetDateTime, hash: String): String {
	for (i in 0 until 3) {
		val id = Photo.generateId(date.toInstant().toEpochMilli(), hash, i)
		if (!this.existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
