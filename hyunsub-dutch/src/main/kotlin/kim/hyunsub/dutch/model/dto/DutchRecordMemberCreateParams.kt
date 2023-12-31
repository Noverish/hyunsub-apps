package kim.hyunsub.dutch.model.dto

data class DutchRecordMemberCreateParams(
	val memberId: String,
	val actual: Int,
	val should: Int,
)
