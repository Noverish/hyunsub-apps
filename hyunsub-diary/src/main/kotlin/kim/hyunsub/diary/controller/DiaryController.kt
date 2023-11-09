package kim.hyunsub.diary.controller

import kim.hyunsub.common.fs.client.FriendServiceClient
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.model.api.ApiDiary
import kim.hyunsub.diary.model.api.toApi
import kim.hyunsub.diary.model.dto.DiaryCreateParams
import kim.hyunsub.diary.model.dto.DiaryUpdateParams
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.diary.repository.findByIdOrNull
import kim.hyunsub.friend.model.dto.MeetFriendBulkUpdateParams
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/diaries")
class DiaryController(
	private val diaryRepository: DiaryRepository,
	private val friendServiceClient: FriendServiceClient,
) {
	@PostMapping("")
	fun create(
		user: UserAuth,
		@RequestBody params: DiaryCreateParams,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary {
		if (diaryRepository.findByIdOrNull(user.idNo, params.date) != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		val diary = Diary(
			userId = user.idNo,
			date = params.date,
			summary = params.summary.trim(),
			content = params.content.trim(),
		)

		diaryRepository.save(diary)

		val meetParams = MeetFriendBulkUpdateParams(params.friendIds)
		val friends = friendServiceClient.updateMeetFriendsBulk(token, diary.date, meetParams)

		return diary.toApi(friends)
	}

	@GetMapping("/{date}")
	fun detail(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary? {
		val diary = diaryRepository.findByIdOrNull(user.idNo, date)
			?: return null

		val friends = friendServiceClient.selectMeetFriends(token, date)

		return diary.toApi(friends)
	}

	@PutMapping("/{date}")
	fun update(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@RequestBody params: DiaryUpdateParams,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary {
		val diary = diaryRepository.findByIdOrNull(user.idNo, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newDiary = diary.copy(
			summary = params.summary.trim(),
			content = params.content.trim(),
		)

		diaryRepository.save(newDiary)

		val meetParams = MeetFriendBulkUpdateParams(params.friendIds)
		val friends = friendServiceClient.updateMeetFriendsBulk(token, date, meetParams)

		return newDiary.toApi(friends)
	}

	@DeleteMapping("/{date}")
	fun delete(
		user: UserAuth,
		@PathVariable date: LocalDate,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
	): ApiDiary {
		val diary = diaryRepository.findByIdOrNull(user.idNo, date)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		diaryRepository.delete(diary)

		val friends = friendServiceClient.selectMeetFriends(token, date)

		return diary.toApi(friends)
	}
}
