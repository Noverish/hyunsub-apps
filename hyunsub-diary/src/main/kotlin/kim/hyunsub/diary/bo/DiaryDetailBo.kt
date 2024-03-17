package kim.hyunsub.diary.bo

import kim.hyunsub.common.fs.client.PhotoServiceClient
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.model.LocalDateRange
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.findByIdOrNull
import kim.hyunsub.diary.service.ApiDiaryService
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryDetailBo(
	private val diaryRepository: DiaryRepository,
	private val photoServiceClient: PhotoServiceClient,
	private val apiDiaryService: ApiDiaryService,
) {
	fun detail(userId: String, token: String, date: LocalDate): ApiDiary? {
		val diary = diaryRepository.findByIdOrNull(userId, date)
			?: return null

		return apiDiaryService.detail(token, diary)
	}

	fun photos(token: String, date: LocalDate, page: Int?): ApiPageResult<ApiPhotoPreview> {
		val dateRange = LocalDateRange(
			start = date,
			end = date
		)
		val params = PhotoSearchParams(dateRange = dateRange)
		return photoServiceClient.searchPhoto(token, params)
	}
}
