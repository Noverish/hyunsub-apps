package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoRepository: JpaRepository<Video, String> {
	fun findByVideoEntryIdIn(videoEntryIds: List<String>): List<Video>

	@Modifying
	@Query("DELETE FROM Video v WHERE v.videoEntryId IN (:videoEntryIds)")
	fun deleteByVideoEntryIdIn(videoEntryIds: List<String>): Int
}
