package kim.hyunsub.dutch.repository.entity

data class DutchRecordMemberWithName(
	val recordId: String,
	val memberId: String,
	val actual: Double,
	val should: Double,
	val name: String,
)
