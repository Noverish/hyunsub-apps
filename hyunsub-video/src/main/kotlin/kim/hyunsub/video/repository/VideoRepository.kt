package kim.hyunsub.video.repository

import kim.hyunsub.common.random.generateRandomString
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEpisode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoRepository : JpaRepository<Video, String> {
	fun findByVideoEntryIdIn(videoEntryIds: List<String>): List<Video>
	fun findByVideoEntryId(videoEntryId: String): List<Video>

	@Query(
		"""
			SELECT a FROM Video a
			WHERE a.videoEntryId = :entryId AND a.videoSeason IS NULL
		"""
	)
	fun findNoSeasonVideos(entryId: String): List<Video>
	fun findByVideoEntryIdAndVideoSeason(videoEntryId: String, videoSeason: String): List<Video>

	@Modifying
	@Query("DELETE FROM Video v WHERE v.videoEntryId IN (:videoEntryIds)")
	fun deleteByVideoEntryIdIn(videoEntryIds: List<String>): Int

	@Query(
		"""
			SELECT new kim.hyunsub.video.repository.entity.VideoEpisode(a.id, b.time, a.path, a.thumbnail, a.videoSeason, c.duration)
			FROM Video AS a
			LEFT JOIN VideoHistory AS b ON b.userId = :userId AND b.videoId = a.id
			LEFT JOIN VideoMetadata c ON c.path = a.path
			WHERE a.videoEntryId = :entryId
			ORDER BY a.path
		"""
	)
	fun selectEpisode(entryId: String, userId: String): List<VideoEpisode>
}

fun VideoRepository.generateId(): String {
	for (i in 0 until 3) {
		val id = generateRandomString(6)
		if (!existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
