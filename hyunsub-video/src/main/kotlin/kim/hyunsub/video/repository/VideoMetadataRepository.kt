package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoMetadata
import org.springframework.data.jpa.repository.JpaRepository

interface VideoMetadataRepository: JpaRepository<VideoMetadata, String>
