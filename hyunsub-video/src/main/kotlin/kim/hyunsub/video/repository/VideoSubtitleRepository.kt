package kim.hyunsub.video.repository

import kim.hyunsub.common.random.generateRandomString
import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull

interface VideoSubtitleRepository : JpaRepository<VideoSubtitle, String> {
	fun findByVideoId(videoId: String): List<VideoSubtitle>

	@Modifying
	@Query("DELETE FROM VideoSubtitle v WHERE v.videoId IN (:videoIds)")
	fun deleteByVideoIdIn(videoIds: List<String>): Int
}

fun VideoSubtitleRepository.generateId(): String {
	for (i in 0 until 3) {
		val newId = generateRandomString(6)
		if (this.findByIdOrNull(newId) == null) {
			return newId
		}
	}
	throw RuntimeException("Failed to generate new id")
}
