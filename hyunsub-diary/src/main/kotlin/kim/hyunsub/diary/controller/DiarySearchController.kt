package kim.hyunsub.diary.controller

import kim.hyunsub.common.fs.client.FriendServiceClient
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.model.api.ApiDiaryPreview
import kim.hyunsub.diary.model.api.toApiPreview
import kim.hyunsub.diary.model.dto.DiarySearchParams
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.friend.model.api.ApiMeetFriendSearchParams
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/search")
class DiarySearchController(
	private val diaryRepository: DiaryRepository,
	private val friendServiceClient: FriendServiceClient,
) {
	@PostMapping("")
	fun search(
		user: UserAuth,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String,
		@RequestBody params: DiarySearchParams,
	): RestApiPageResult<ApiDiaryPreview> {
		val userId = user.idNo
		val page = PageRequest.of(params.page, params.pageSize)

		val (total, result) = when {
			params.dates != null -> searchByDates(userId, page, params.dates)
			else -> searchByQuery(userId, page, params.query ?: "")
		}

		val dates = result.map { it.date }
		val dateFriendsMap = friendServiceClient.searchMeetFriends(token, ApiMeetFriendSearchParams(dates))
			.associate { it.date to it.friends }

		return RestApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = result.map {
				it.toApiPreview(dateFriendsMap[it.date] ?: emptyList())
			},
		)
	}

	private fun searchByQuery(
		userId: String,
		page: Pageable,
		query: String,
	): Pair<Int, List<Diary>> {
		val total = diaryRepository.countByQuery(userId, query)
		val result = diaryRepository.selectByQuery(userId, query, page)
		return total to result
	}

	private fun searchByDates(
		userId: String,
		page: Pageable,
		dates: List<LocalDate>,
	): Pair<Int, List<Diary>> {
		val total = diaryRepository.countByDates(userId, dates)
		val result = diaryRepository.selectByDates(userId, dates, page)
		return total to result
	}
}
