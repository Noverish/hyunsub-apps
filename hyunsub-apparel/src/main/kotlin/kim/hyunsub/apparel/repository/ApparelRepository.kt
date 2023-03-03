package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.Apparel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApparelRepository : JpaRepository<Apparel, String> {
	fun findByIdAndUserId(apparelId: String, userId: String): Apparel?

	@Query("SELECT distinct category FROM Apparel WHERE userId = :userId")
	fun findCategories(userId: String): List<String>

	@Query("SELECT distinct brand FROM Apparel WHERE userId = :userId")
	fun findBrands(userId: String): List<String?>
}
