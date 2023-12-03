package kim.hyunsub.diary.model.api

import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import java.time.LocalDate

data class ApiDiary(
	val date: LocalDate,
	val summary: String,
	val content: String,
	val friends: List<ApiFriendPreview>,
	val photoNum: Int,
	val photos: List<ApiPhotoPreview>,
)
