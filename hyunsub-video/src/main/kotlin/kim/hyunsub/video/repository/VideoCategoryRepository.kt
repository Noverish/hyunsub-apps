package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoCategory
import org.springframework.data.jpa.repository.JpaRepository

interface VideoCategoryRepository: JpaRepository<VideoCategory, Int>
