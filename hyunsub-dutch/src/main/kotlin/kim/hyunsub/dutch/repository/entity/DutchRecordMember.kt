package kim.hyunsub.dutch.repository.entity

import java.time.LocalDateTime

data class DutchRecordMember(
	val recordId: String,
	val memberId: String,
	val actual: Double,
	val should: Double,
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
)
