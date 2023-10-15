package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoMetadata
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoMetadataRepository : JpaRepository<VideoMetadata, String> {
	@Modifying
	@Query("UPDATE VideoMetadata SET path = :to WHERE path = :from")
	fun updatePath(from: String, to: String): Int
}
