package kim.hyunsub.video.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoSubtitleRepository : JpaRepository<VideoSubtitle, String> {
	fun findByVideoId(videoId: String): List<VideoSubtitle>
	fun findByVideoIdIn(videoIds: List<String>): List<VideoSubtitle>

	@Modifying
	@Query("DELETE FROM VideoSubtitle v WHERE v.videoId IN (:videoIds)")
	fun deleteByVideoIdIn(videoIds: List<String>): Int
}

fun VideoSubtitleRepository.generateId() = generateId(6)
