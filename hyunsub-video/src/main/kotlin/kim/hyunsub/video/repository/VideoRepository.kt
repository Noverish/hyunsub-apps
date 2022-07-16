package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.Video
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository: JpaRepository<Video, String>
