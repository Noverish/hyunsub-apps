package kim.hyunsub.diary.service

import kim.hyunsub.common.fs.client.FriendServiceClient
import kim.hyunsub.common.fs.client.PhotoServiceClient
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.friend.model.api.ApiFriendPreview
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import org.springframework.stereotype.Service

@Service
class ApiDiaryService(
	private val friendServiceClient: FriendServiceClient,
	private val photoServiceClient: PhotoServiceClient,
) {
	fun detail(token: String, diary: Diary, friends: List<ApiFriendPreview>? = null): ApiDiary {
		val date = diary.date

		val friends2 = friends ?: friendServiceClient.selectMeetFriends(token, date)

		val photoParams = PhotoSearchParams(date, true, 0, 12)
		val photoResult = photoServiceClient.searchPhoto(token, photoParams)

		return ApiDiary(
			date = date,
			summary = diary.summary,
			content = diary.content,
			friends = friends2,
			photoNum = photoResult.total,
			photos = photoResult.data,
		)
	}
}
