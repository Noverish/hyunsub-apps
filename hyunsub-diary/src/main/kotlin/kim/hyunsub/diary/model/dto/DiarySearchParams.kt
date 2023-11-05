package kim.hyunsub.diary.model.dto

import java.time.LocalDate

data class DiarySearchParams(
	val query: String?,
	val dates: List<LocalDate>?,
	val page: Int = 0,
	val pageSize: Int = 10,
)
