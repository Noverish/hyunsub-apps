package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency
import java.time.LocalDateTime

data class ApiDutchRecord(
	val id: String,
	val content: String,
	val location: String,
	val currency: DutchCurrency,
	val date: LocalDateTime,
	val amount: Double,
	val members: List<String>,
)
