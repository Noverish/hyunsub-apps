package kim.hyunsub.diary.model.api

import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.friend.model.api.ApiFriendPreview
import java.time.LocalDate

data class ApiDiaryPreview(
	val date: LocalDate,
	val summary: String,
	val content: String,
	val friends: List<ApiFriendPreview>,
)

fun Diary.toApiPreview(friends: List<ApiFriendPreview>) = ApiDiaryPreview(
	date = date,
	summary = summary,
	content = content,
	friends = friends,
)
