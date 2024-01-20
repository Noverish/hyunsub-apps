package kim.hyunsub.dutch.repository.entity

import kim.hyunsub.dutch.model.DutchCurrency
import java.time.LocalDateTime

data class DutchSpend(
	val recordId: String,
	val content: String,
	val location: String,
	val date: LocalDateTime,
	val currency: DutchCurrency,
	val actual: Double,
	val should: Double,
)
