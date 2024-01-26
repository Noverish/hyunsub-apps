package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchBalanceCreateParams(
	val currency: DutchCurrency,
	val amount: Double,
)
