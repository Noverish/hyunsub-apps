package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PhotoOwnerRepository : JpaRepository<PhotoOwner, PhotoOwnerId> {
	@Query(
		"""
			SELECT a FROM PhotoOwner a
			WHERE a.userId = :userId AND a.name LIKE CONCAT(:name, '%')
		"""
	)
	fun selectMyPhotoByName(userId: String, name: String, page: Pageable = Pageable.unpaged()): List<PhotoOwner>

	fun countByUserId(userId: String): Int

	fun countByPhotoId(photoId: String): Int

	fun findByUserId(userId: String): List<PhotoOwner>

	@Modifying
	@Query("UPDATE PhotoOwner SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
