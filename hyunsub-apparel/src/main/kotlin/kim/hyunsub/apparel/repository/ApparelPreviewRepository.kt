package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.ApparelPreview
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelPreviewRepository : JpaRepository<ApparelPreview, String> {
	fun findByUserId(userId: String, page: Pageable = Pageable.unpaged()): List<ApparelPreview>
	fun countByUserId(userId: String): Int

	fun findByCategoryAndUserId(category: String, userId: String, page: Pageable = Pageable.unpaged()): List<ApparelPreview>
	fun countByCategoryAndUserId(category: String, userId: String): Int

	fun findByBrandAndUserId(brand: String, userId: String, page: Pageable = Pageable.unpaged()): List<ApparelPreview>
	fun countByBrandAndUserId(brand: String, userId: String): Int
}
