package kim.hyunsub.diary.bo

import kim.hyunsub.common.fs.client.FriendServiceClient
import kim.hyunsub.common.fs.client.PhotoServiceClient
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.diary.model.api.toApi
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.findByIdOrNull
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryDetailBo(
	private val diaryRepository: DiaryRepository,
	private val friendServiceClient: FriendServiceClient,
	private val photoServiceClient: PhotoServiceClient,
) {
	fun detail(userId: String, token: String, date: LocalDate): ApiDiary? {
		val diary = diaryRepository.findByIdOrNull(userId, date)
			?: return null

		val friends = friendServiceClient.selectMeetFriends(token, date)

		return diary.toApi(friends)
	}

	fun photos(token: String, date: LocalDate, page: Int?): RestApiPageResult<ApiPhotoPreview> {
		val params = PhotoSearchParams(date, false, page, null)
		return photoServiceClient.searchPhoto(token, params)
	}
}
