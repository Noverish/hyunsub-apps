package kim.hyunsub.diary.model

data class DiarySearchQuery(
	val query: String,
	val page: Int = 0,
	val pageSize: Int = 10,
)
