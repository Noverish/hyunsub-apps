package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.common.random.generateRandomString
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelImageRepository : JpaRepository<ApparelImage, String> {
	fun findByApparelId(apparelId: String): List<ApparelImage>
}

fun ApparelImageRepository.generateId(): String {
	for (i in 0 until 3) {
		val id = generateRandomString(10)
		if (!existsById(id)) {
			return id
		}
	}
	throw RuntimeException("Failed to generate new id")
}
