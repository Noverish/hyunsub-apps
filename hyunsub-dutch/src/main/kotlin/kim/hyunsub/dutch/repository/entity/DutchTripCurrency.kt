package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchTripCurrency(
	val tripId: String,
	val currency: DutchCurrency,
	val rate: String,
)
