package kim.hyunsub.diary.model.dto

data class DiarySearchParams(
	val query: String?,
	val page: Int = 0,
	val pageSize: Int = 10,
)
