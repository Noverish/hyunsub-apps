package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.repository.entity.DutchTrip

data class ApiDutchTrip(
	val id: String,
	val name: String,
	val currency: DutchCurrency,
)

fun DutchTrip.toApi() = ApiDutchTrip(
	id = id,
	name = name,
	currency = currency,
)
