package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.ApparelImage
import kim.hyunsub.common.util.generateId
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelImageRepository : JpaRepository<ApparelImage, String> {
	fun findByApparelIdOrderByRegDt(apparelId: String): List<ApparelImage>
}

fun ApparelImageRepository.generateId() = generateId(10)
