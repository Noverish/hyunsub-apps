package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.model.DutchPayment
import java.time.LocalDateTime

data class DutchRecordParams(
	val content: String,
	val location: String,
	val currency: DutchCurrency,
	val payment: DutchPayment,
	val date: LocalDateTime,
	val members: List<DutchRecordMemberParams>,
)
