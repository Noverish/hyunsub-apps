package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PhotoMetadataRepository : JpaRepository<PhotoMetadata, String> {
	@Modifying
	@Query("UPDATE PhotoMetadata SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
