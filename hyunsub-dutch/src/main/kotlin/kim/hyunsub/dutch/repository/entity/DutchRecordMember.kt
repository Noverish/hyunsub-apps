package kim.hyunsub.dutch.repository.entity

data class DutchRecordMember(
	val recordId: String,
	val memberId: String,
	val actual: Double,
	val should: Double,
)
