package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency

data class ApiDutchBalance(
	val currency: DutchCurrency,
	val amount: Double?,
	val spends: Double,
)
