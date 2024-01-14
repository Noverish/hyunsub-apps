package kim.hyunsub.dutch.model.dto

import kim.hyunsub.dutch.model.DutchCurrency

data class DutchRecordSearchParams(
	val tripId: String,
	val query: String?,
	val currency: DutchCurrency?,
	val page: Int?,
	val pageSize: Int?,
) {
	val limit: Int
		get() = pageSize ?: 25

	val offset: Int
		get() = (page ?: 0) * (pageSize ?: 25)
}
