package kim.hyunsub.dutch.model.dto

data class DutchSettleResult(
	val members: List<DutchSettleMemberResult>,
)

data class DutchSettleMemberResult(
	val memberId: String,
	val amount: Double,
)
