package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchSettleMemberTempResult(
	val memberId: String,
	val currency: DutchCurrency,
	val actual: Double,
	val should: Double,
)
