package kim.hyunsub.video.repository

import kim.hyunsub.video.repository.entity.VideoGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface VideoGroupRepository: JpaRepository<VideoGroup, String> {
	@Modifying
	@Query("DELETE FROM VideoGroup v WHERE v.id IN (:ids)")
	fun deleteByIdIn(ids: List<String>): Int

	fun findByCategoryIdIn(categoryIds: List<Int>): List<VideoGroup>
	fun findByName(name: String): VideoGroup?
}
