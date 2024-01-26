package kim.hyunsub.dutch.model.dto

import org.springframework.data.domain.Pageable

data class DutchSpendSearchQuery(
	val memberId: String? = null,
	val page: Pageable? = null,
)
