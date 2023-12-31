package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchTripCreateParams(
	val name: String,
	val currency: DutchCurrency,
	val members: List<String>,
)
