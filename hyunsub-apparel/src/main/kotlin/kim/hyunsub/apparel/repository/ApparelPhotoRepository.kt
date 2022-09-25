package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.ApparelPhoto
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelPhotoRepository: JpaRepository<ApparelPhoto, Int> {
	fun findByApparelId(apparelId: String): List<ApparelPhoto>
}
