package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.MyPhoto
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PhotoOwnerRepository : JpaRepository<PhotoOwner, PhotoOwnerId> {
	@Query(
		"""
		SELECT new kim.hyunsub.photo.repository.entity.MyPhoto(b.id, b.hash, b.width, b.height, b.size, b.offset, b.original, b.ext, a.regDt)
		FROM PhotoOwner a
		INNER JOIN PhotoV2 b ON b.id = a.photoId
		WHERE a.userId = :userId
	"""
	)
	fun selectMyPhotos(userId: String): List<MyPhoto>
}
