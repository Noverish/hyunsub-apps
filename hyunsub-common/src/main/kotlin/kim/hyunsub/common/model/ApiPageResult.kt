package kim.hyunsub.common.model

data class ApiPageResult<T>(
	val total: Int,
	val page: Int,
	val pageSize: Int,
	val data: List<T>,
) {
	companion object {
		fun <T> empty() = ApiPageResult<T>(
			total = 0,
			page = 0,
			pageSize = 0,
			data = emptyList(),
		)
	}
}
