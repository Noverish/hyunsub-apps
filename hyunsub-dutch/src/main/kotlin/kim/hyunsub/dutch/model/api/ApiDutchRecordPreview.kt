package kim.hyunsub.dutch.model.api

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.model.DutchPayment
import java.time.LocalDateTime

data class ApiDutchRecordPreview(
	val id: String,
	val content: String,
	val location: String,
	val currency: DutchCurrency,
	val payment: DutchPayment,
	val date: LocalDateTime,
	val amount: Double,
	val members: List<String>,
)
