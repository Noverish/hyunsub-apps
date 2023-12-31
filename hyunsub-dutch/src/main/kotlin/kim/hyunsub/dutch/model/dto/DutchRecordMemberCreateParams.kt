package kim.hyunsub.dutch.model.dto

data class DutchRecordMemberCreateParams(
	val memberId: String,
	val actual: Double,
	val should: Double,
)
