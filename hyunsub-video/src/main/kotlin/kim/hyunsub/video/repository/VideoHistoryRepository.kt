package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoEntryHistory
import kim.hyunsub.video.repository.entity.VideoHistory
import kim.hyunsub.video.repository.entity.VideoHistoryId
import kim.hyunsub.video.repository.entity.VideoMyHistory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VideoHistoryRepository : JpaRepository<VideoHistory, VideoHistoryId> {
	@Query(
		"""
			SELECT new kim.hyunsub.video.repository.entity.VideoMyHistory(a.videoId, a.time, a.date, b.entryId, c.duration, b.thumbnail, b.path)
			FROM VideoHistory a
			INNER JOIN Video b ON b.id = a.videoId
			LEFT JOIN VideoMetadata  c ON c.path = b.path
			WHERE a.userId = :userId
			ORDER BY a.date DESC
		"""
	)
	fun selectHistories(userId: String, page: Pageable = Pageable.unpaged()): List<VideoMyHistory>

	@Query(
		value = """
			WITH A AS (
				SELECT a.user_id, a.video_id, a.time, a.date, b.entry_id, b.path
				FROM video_history a
				INNER JOIN video b ON b.id = a.video_id
				WHERE a.user_id = :userId
			), B AS (
				SELECT entry_id, MAX(date) AS date
				FROM A
				GROUP BY entry_id
				ORDER BY date DESC
				LIMIT 12
			)
			SELECT b.entry_id AS entryId, b.date, a.video_id AS videoId, a.time, a.path, c.name, c.thumbnail, c.category, d.duration
			FROM B b
			INNER JOIN A a ON a.entry_id = b.entry_id AND a.date = b.date
			INNER JOIN video_entry c ON c.id = a.entry_id
			INNER JOIN video_metadata d ON d.path = a.path;
		""",
		nativeQuery = true,
	)
	fun selectHistories2(userId: String): List<VideoEntryHistory>

	fun countByUserId(userId: String): Int

	fun findByVideoId(videoId: String): List<VideoHistory>
}
