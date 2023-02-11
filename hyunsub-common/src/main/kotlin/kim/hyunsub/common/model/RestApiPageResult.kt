package kim.hyunsub.common.model

data class RestApiPageResult<T>(
	val total: Int,
	val page: Int,
	val pageSize: Int,
	val data: List<T>,
) {
	companion object {
		fun <T> empty() = RestApiPageResult<T>(
			total = 0,
			page = 0,
			pageSize = 0,
			data = emptyList(),
		)
	}
}
