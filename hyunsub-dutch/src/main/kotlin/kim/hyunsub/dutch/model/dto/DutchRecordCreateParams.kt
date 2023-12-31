package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency
import java.time.LocalDateTime

data class DutchRecordCreateParams(
	val content: String,
	val location: String,
	val currency: DutchCurrency,
	val date: LocalDateTime,
	val members: List<DutchRecordMemberCreateParams>,
)
