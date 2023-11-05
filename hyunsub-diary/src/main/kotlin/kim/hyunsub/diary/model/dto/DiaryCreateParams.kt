package kim.hyunsub.diary.model.dto

import java.time.LocalDate

data class DiaryCreateParams(
	val date: LocalDate,
	val content: String,
	val summary: String,
	val friendIds: List<String>,
)
