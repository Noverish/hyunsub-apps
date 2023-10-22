package kim.hyunsub.diary.model.api

import kim.hyunsub.diary.repository.entity.Diary
import java.time.LocalDate

data class ApiDiaryPreview(
	val date: LocalDate,
	val summary: String,
	val content: String,
)

fun Diary.toApiPreview() = ApiDiaryPreview(
	date = date,
	summary = summary,
	content = content,
)
