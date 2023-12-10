package kim.hyunsub.video.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEpisode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoRepository : JpaRepository<Video, String> {
	fun findByEntryIdIn(entryIds: List<String>): List<Video>
	fun findByEntryId(entryId: String): List<Video>
	fun findByPath(path: String): Video?

	@Query(
		"""
			SELECT a FROM Video a
			WHERE a.entryId = :entryId AND a.season IS NULL
		"""
	)
	fun findNoSeasonVideos(entryId: String): List<Video>
	fun findByEntryIdAndSeason(entryId: String, season: String): List<Video>

	@Modifying
	@Query("DELETE FROM Video v WHERE v.entryId IN (:entryIds)")
	fun deleteByEntryIdIn(entryIds: List<String>): Int

	@Query(
		"""
			SELECT new kim.hyunsub.video.repository.entity.VideoEpisode(a.id, b.time, a.path, a.thumbnail, a.season, c.duration)
			FROM Video AS a
			LEFT JOIN VideoHistory AS b ON b.userId = :userId AND b.videoId = a.id
			LEFT JOIN VideoMetadata c ON c.path = a.path
			WHERE a.entryId = :entryId
			ORDER BY a.path
		"""
	)
	fun selectEpisode(entryId: String, userId: String): List<VideoEpisode>
}

fun VideoRepository.generateId() = generateId(6)
