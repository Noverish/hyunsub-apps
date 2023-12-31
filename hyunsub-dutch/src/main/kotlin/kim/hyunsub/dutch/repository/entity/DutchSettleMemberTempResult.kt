package kim.hyunsub.dutch.repository.entity

data class DutchSettleMemberTempResult(
	val memberId: String,
	val actual: Double,
	val should: Double,
)
