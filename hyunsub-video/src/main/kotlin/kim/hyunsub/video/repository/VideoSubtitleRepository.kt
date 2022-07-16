package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.data.jpa.repository.JpaRepository

interface VideoSubtitleRepository: JpaRepository<VideoSubtitle, String>
