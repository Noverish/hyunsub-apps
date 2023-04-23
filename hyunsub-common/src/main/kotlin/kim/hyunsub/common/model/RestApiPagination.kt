package kim.hyunsub.common.model

data class RestApiPagination<T>(
	val total: Int,
	val prev: String?,
	val next: String?,
	val data: List<T>,
)
