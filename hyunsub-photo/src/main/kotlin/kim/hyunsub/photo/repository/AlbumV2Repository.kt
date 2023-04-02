package kim.hyunsub.photo.repository

import kim.hyunsub.common.random.generateRandomString
import kim.hyunsub.photo.repository.entity.AlbumV2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

interface AlbumV2Repository : JpaRepository<AlbumV2, String> {
	@Query(
		"""
			SELECT b FROM AlbumOwner a
			INNER JOIN AlbumV2 b ON b.id = a.albumId
			WHERE a.userId = :userId
		"""
	)
	fun findByUserId(userId: String): List<AlbumV2>
}

fun AlbumV2Repository.generateId(): String {
	for (i in 0 until 3) {
		val newId = generateRandomString(8)
		if (this.findByIdOrNull(newId) == null) {
			return newId
		}
	}
	throw RuntimeException("Failed to generate new id")
}
