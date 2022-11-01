package kim.hyunsub.division.model.dto

import kim.hyunsub.division.model.CurrencyCode
import java.time.LocalDateTime

data class RestApiRecord(
	val id: String,
	val content: String,
	val location: String,
	val currency: CurrencyCode,
	val date: LocalDateTime,
	val gatheringId: String,
	val shares: List<RestApiRecordShare>,
)
