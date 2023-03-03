package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.ApparelImage
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelImageRepository : JpaRepository<ApparelImage, String> {
	fun findByApparelId(apparelId: String): List<ApparelImage>
	fun findByIdAndApparelId(id: String, apparelId: String): ApparelImage?
}
