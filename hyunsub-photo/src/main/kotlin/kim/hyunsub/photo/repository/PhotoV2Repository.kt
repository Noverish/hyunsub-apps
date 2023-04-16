package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoV2
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.OffsetDateTime

interface PhotoV2Repository : JpaRepository<PhotoV2, String> {
	@Query(
		"""
			SELECT b FROM PhotoOwner a
			INNER JOIN PhotoV2 b ON b.id = a.photoId
			WHERE a.userId = :userId
			ORDER BY a.photoId DESC
		"""
	)
	fun selectMyPhotos(userId: String, page: Pageable = Pageable.unpaged()): List<PhotoV2>

	fun findByHash(hash: String): PhotoV2?

	@Modifying
	@Query("UPDATE PhotoV2 SET id = :to where id = :from")
	fun updateId(from: String, to: String): Int
}

fun PhotoV2Repository.generateId(date: OffsetDateTime, hash: String): String {
	for (i in 0 until 3) {
		val id = PhotoV2.generateId(date.toInstant().toEpochMilli(), hash, i)
		if (!this.existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
