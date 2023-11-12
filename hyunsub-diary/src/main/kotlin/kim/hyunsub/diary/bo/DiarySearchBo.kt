package kim.hyunsub.diary.bo

import kim.hyunsub.common.fs.client.FriendServiceClient
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.diary.model.api.ApiDiaryPreview
import kim.hyunsub.diary.model.api.toApiPreview
import kim.hyunsub.diary.model.dto.DiarySearchParams
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
import kim.hyunsub.friend.model.api.ApiMeetFriendSearchParams
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DiarySearchBo(
	private val diaryRepository: DiaryRepository,
	private val friendServiceClient: FriendServiceClient,
) {
	fun search(userId: String, token: String, params: DiarySearchParams?): ApiPageResult<ApiDiaryPreview> {
		val dates = params?.dates
		val query = params?.query ?: ""
		val page = params?.page ?: 0
		val pageSize = params?.pageSize ?: 10

		val pageRequest = PageRequest.of(page, pageSize)

		val (total, result) = when {
			dates != null -> searchByDates(userId, pageRequest, dates)
			else -> searchByQuery(userId, pageRequest, query)
		}

		val resultDates = result.map { it.date }
		val dateFriendsMap = friendServiceClient.searchMeetFriends(token, ApiMeetFriendSearchParams(resultDates))
			.associate { it.date to it.friends }

		return ApiPageResult(
			total = total,
			page = pageRequest.pageNumber,
			pageSize = pageRequest.pageSize,
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
