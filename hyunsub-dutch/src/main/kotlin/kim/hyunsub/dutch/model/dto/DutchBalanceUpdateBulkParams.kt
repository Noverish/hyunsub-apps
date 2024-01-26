package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchBalanceUpdateBulkParams(
	val data: List<DutchBalanceUpdateBulkData>,
)

data class DutchBalanceUpdateBulkData(
	val currency: DutchCurrency,
	val amount: Double,
)
