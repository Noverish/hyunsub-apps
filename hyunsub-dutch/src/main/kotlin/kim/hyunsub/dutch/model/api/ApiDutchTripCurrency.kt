package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency

data class ApiDutchTripCurrency(
	val currency: DutchCurrency,
	val rate: Double?,
)
