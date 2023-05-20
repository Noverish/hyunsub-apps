package kim.hyunsub.video.repository

import kim.hyunsub.common.random.generateRandomString
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

fun VideoSubtitleRepository.generateId(): String {
	for (i in 0 until 3) {
		val id = generateRandomString(6)
		if (!existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
