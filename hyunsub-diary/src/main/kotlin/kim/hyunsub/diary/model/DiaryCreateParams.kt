package kim.hyunsub.diary.model

import java.time.LocalDate

data class DiaryCreateParams(
	val date: LocalDate,
	val content: String,
	val summary: String?,
)
