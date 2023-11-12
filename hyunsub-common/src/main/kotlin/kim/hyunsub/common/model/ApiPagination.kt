package kim.hyunsub.common.model

data class ApiPagination<T>(
	val total: Int,
	val prev: String?,
	val next: String?,
	val data: List<T>,
)
