package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchSettleResult(
	val currency: DutchCurrency,
	val shares: List<DutchSettleResultShare>,
)

data class DutchSettleResultShare(
	val memberId: String,
	val amount: Double,
)
