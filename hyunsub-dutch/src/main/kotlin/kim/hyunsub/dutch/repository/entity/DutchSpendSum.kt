package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchSpendSum(
	val currency: DutchCurrency,
	val actual: Double,
	val should: Double,
)
