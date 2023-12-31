package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.repository.entity.DutchRecord
import java.time.LocalDateTime

data class ApiDutchRecord(
	val id: String,
	val content: String,
	val location: String,
	val currency: DutchCurrency,
	val date: LocalDateTime,
	val tripId: String,
)

fun DutchRecord.toApi() = ApiDutchRecord(
	id = id,
	content = content,
	location = location,
	currency = currency,
	date = date,
	tripId = tripId,
)
