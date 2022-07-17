package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoEntry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoEntryRepository: JpaRepository<VideoEntry, String> {
	fun findByCategory(category: String): List<VideoEntry>

	@Modifying
	@Query("DELETE FROM VideoEntry v WHERE v.category = :category")
	fun deleteByCategory(category: String): Int
}
