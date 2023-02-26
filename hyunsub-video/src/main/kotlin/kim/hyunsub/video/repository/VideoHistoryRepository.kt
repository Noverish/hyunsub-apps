package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoHistory
import kim.hyunsub.video.repository.entity.VideoHistoryId
import org.springframework.data.jpa.repository.JpaRepository

interface VideoHistoryRepository : JpaRepository<VideoHistory, VideoHistoryId> {
	fun findByUserId(userId: String): List<VideoHistory>
}
