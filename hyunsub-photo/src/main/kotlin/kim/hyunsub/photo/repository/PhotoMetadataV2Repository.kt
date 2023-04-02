package kim.hyunsub.photo.repository

import kim.hyunsub.photo.repository.entity.PhotoMetadataV2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PhotoMetadataV2Repository : JpaRepository<PhotoMetadataV2, String> {
	@Modifying
	@Query("UPDATE PhotoMetadataV2 SET photoId = :to WHERE photoId = :from")
	fun updatePhotoId(from: String, to: String): Int
}
