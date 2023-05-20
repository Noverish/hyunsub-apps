package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.Apparel
import kim.hyunsub.common.random.generateRandomString
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ApparelRepository : JpaRepository<Apparel, String> {
	fun findByIdAndUserId(apparelId: String, userId: String): Apparel?

	@Query("SELECT distinct category FROM Apparel WHERE userId = :userId")
	fun findCategories(userId: String): List<String>

	@Query("SELECT distinct brand FROM Apparel WHERE userId = :userId")
	fun findBrands(userId: String): List<String?>
}

fun ApparelRepository.generateId(): String {
	for (i in 0 until 3) {
		val id = generateRandomString(8)
		if (!existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
