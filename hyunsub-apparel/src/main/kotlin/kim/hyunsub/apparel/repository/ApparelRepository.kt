package kim.hyunsub.apparel.repository

import kim.hyunsub.apparel.repository.entity.Apparel
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ApparelRepository: JpaRepository<Apparel, String> {
	fun findByIdAndUserId(apparelId: String, userId: String): Apparel?
}
