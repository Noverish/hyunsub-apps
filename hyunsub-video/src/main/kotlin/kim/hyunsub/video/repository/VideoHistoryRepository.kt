package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoHistory
import kim.hyunsub.video.repository.entity.VideoHistoryId
import kim.hyunsub.video.repository.entity.VideoMyHistory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VideoHistoryRepository : JpaRepository<VideoHistory, VideoHistoryId> {
	@Query(
		"""
			SELECT new kim.hyunsub.video.repository.entity.VideoMyHistory(a.videoId, a.time, a.date, b.videoEntryId, c.duration, b.thumbnail, b.path)
			FROM VideoHistory a
			INNER JOIN Video b ON b.id = a.videoId
			LEFT JOIN VideoMetadata  c ON c.path = b.path
			WHERE a.userId = :userId
			ORDER BY a.date DESC
		"""
	)
	fun selectHistories(userId: String, page: Pageable = Pageable.unpaged()): List<VideoMyHistory>

	fun countByUserId(userId: String): Int

	fun findByVideoId(videoId: String): List<VideoHistory>
}
