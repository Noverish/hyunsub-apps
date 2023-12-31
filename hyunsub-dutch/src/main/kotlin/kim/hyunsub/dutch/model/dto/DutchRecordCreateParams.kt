package kim.hyunsub.dutch.model.dto

import java.time.LocalDateTime

data class DutchRecordCreateParams(
	val content: String,
	val location: String,
	val currency: String,
	val date: LocalDateTime,
	val members: List<DutchRecordMemberCreateParams>,
)
