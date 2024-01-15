package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchTrip(
	val id: String,
	val name: String,
	val currency: DutchCurrency,
)
