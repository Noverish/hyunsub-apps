package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.repository.entity.DutchTrip

data class ApiDutchTrip(
	val id: String,
	val name: String,
	val tripCurrency: DutchCurrency,
	val settleCurrency: DutchCurrency,
)

fun DutchTrip.toApi() = ApiDutchTrip(
	id = id,
	name = name,
	tripCurrency = tripCurrency,
	settleCurrency = settleCurrency,
)
