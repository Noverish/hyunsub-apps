package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchPayment
import org.springframework.data.domain.Pageable

data class DutchSpendSearchQuery(
	val memberId: String? = null,
	val payment: DutchPayment? = null,
	val page: Pageable? = null,
)
