package kim.hyunsub.video.repository

import kim.hyunsub.common.util.generateId
import kim.hyunsub.video.repository.entity.VideoEntry
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoEntryRepository : JpaRepository<VideoEntry, String> {
	fun findByCategory(category: String, page: Pageable = Pageable.unpaged()): List<VideoEntry>
	fun countByCategory(category: String): Int

	fun findByCategoryOrderByRegDtDesc(category: String, page: Pageable = Pageable.unpaged()): List<VideoEntry>

	fun findByVideoGroupId(videoGroupId: String): List<VideoEntry>
	fun findByNameContaining(query: String): List<VideoEntry>

	@Query(value = "SELECT * FROM video_entry WHERE category = :category ORDER BY RAND(:seed)", nativeQuery = true)
	fun findByCategoryOrderByRand(category: String, seed: Int, page: Pageable = Pageable.unpaged()): List<VideoEntry>

	@Modifying
	@Query("DELETE FROM VideoEntry v WHERE v.category = :category")
	fun deleteByCategory(category: String): Int
}

fun VideoEntryRepository.generateId() = generateId(6)
