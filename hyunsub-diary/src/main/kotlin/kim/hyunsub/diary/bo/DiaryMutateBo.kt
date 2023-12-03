package kim.hyunsub.diary.bo

import kim.hyunsub.common.fs.client.FriendServiceClient
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.diary.model.dto.DiaryCreateParams
import kim.hyunsub.diary.model.dto.DiaryUpdateParams
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.diary.repository.findByIdOrNull
import kim.hyunsub.diary.service.ApiDiaryService
import kim.hyunsub.friend.model.dto.MeetFriendBulkUpdateParams
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiaryMutateBo(
	private val diaryRepository: DiaryRepository,
	private val friendServiceClient: FriendServiceClient,
	private val apiDiaryService: ApiDiaryService,
) {
	fun create(userId: String, token: String, params: DiaryCreateParams): ApiDiary {
		if (diaryRepository.findByIdOrNull(userId, params.date) != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		val diary = Diary(
			userId = userId,
			date = params.date,
			summary = params.summary.trim(),
			content = params.content.trim(),
		)

		diaryRepository.save(diary)

		val meetParams = MeetFriendBulkUpdateParams(params.friendIds)
		val friends = friendServiceClient.updateMeetFriendsBulk(token, diary.date, meetParams)

		return apiDiaryService.detail(token, diary, friends)
	}

	fun update(userId: String, token: String, date: LocalDate, params: DiaryUpdateParams): ApiDiary {
		val diary = diaryRepository.findByIdOrNull(userId, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newDiary = diary.copy(
			summary = params.summary.trim(),
			content = params.content.trim(),
		)

		diaryRepository.save(newDiary)

		val meetParams = MeetFriendBulkUpdateParams(params.friendIds)
		val friends = friendServiceClient.updateMeetFriendsBulk(token, date, meetParams)

		return apiDiaryService.detail(token, diary, friends)
	}

	fun delete(userId: String, token: String, date: LocalDate): ApiDiary {
		val diary = diaryRepository.findByIdOrNull(userId, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		diaryRepository.delete(diary)

		return apiDiaryService.detail(token, diary)
	}
}
