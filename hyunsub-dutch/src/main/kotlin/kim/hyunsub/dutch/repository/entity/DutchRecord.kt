package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.model.DutchPayment
import java.time.LocalDateTime

data class DutchRecord(
	val id: String,
	val content: String,
	val location: String,
	val currency: DutchCurrency,
	val payment: DutchPayment,
	val date: LocalDateTime,
	val tripId: String,
)
