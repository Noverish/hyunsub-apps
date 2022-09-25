package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.Apparel
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelRepository: JpaRepository<Apparel, String> {
	fun findByUserId(userId: String): List<Apparel>
}
