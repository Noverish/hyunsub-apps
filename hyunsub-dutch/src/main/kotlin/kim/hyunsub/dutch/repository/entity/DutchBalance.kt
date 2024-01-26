package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchBalance(
	val memberId: String,
	val currency: DutchCurrency,
	val amount: Double,
)
