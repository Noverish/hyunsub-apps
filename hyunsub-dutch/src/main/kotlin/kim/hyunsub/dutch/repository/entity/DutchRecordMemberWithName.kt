package kim.hyunsub.dutch.repository.entity

import java.time.LocalDateTime

data class DutchRecordMemberWithName(
	val recordId: String,
	val memberId: String,
	val actual: Double,
	val should: Double,
	val regDt: LocalDateTime = LocalDateTime.now().withNano(0),
	val name: String,
)
