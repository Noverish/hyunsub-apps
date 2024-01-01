package kim.hyunsub.dutch.model.dto

data class DutchSearchParams(
	val tripId: String,
	val query: String?,
	val page: Int?,
	val pageSize: Int?,
) {
	val limit: Int
		get() = pageSize ?: 25

	val offset: Int
		get() = (page ?: 0) * (pageSize ?: 25)
}
