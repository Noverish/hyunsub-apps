package kim.hyunsub.common.model

data class RestApiPageResult<T>(
	val total: Int,
	val page: Int,
	val pageSize: Int,
	val data: List<T>,
) {
	val start: Int
		get() = page * pageSize

	val end: Int
		get() = start + data.size - 1
}
