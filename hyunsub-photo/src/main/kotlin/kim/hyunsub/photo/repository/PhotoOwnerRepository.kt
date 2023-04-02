package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import kim.hyunsub.photo.repository.entity.PhotoV2
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PhotoOwnerRepository : JpaRepository<PhotoOwner, PhotoOwnerId> {
	@Query(
		"""
		SELECT b FROM PhotoOwner a
		INNER JOIN PhotoV2 b ON b.id = a.photoId
		WHERE a.userId = :userId
		ORDER BY a.photoId DESC
	"""
	)
	fun selectMyPhotos(userId: String, page: Pageable = Pageable.unpaged()): List<PhotoV2>

	fun countByUserId(userId: String): Int

	fun countByPhotoId(photoId: String): Int

	@Modifying
	@Query("UPDATE PhotoOwner SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
