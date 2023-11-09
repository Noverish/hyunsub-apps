package kim.hyunsub.diary.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.model.api.ApiDiaryPreview
import kim.hyunsub.diary.model.api.toApiPreview
import kim.hyunsub.diary.model.dto.DiarySearchParams
import kim.hyunsub.diary.repository.DiaryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1/search")
class DiarySearchController(
	private val diaryRepository: DiaryRepository,
) {
	@PostMapping("")
	fun search(
		user: UserAuth,
		@RequestBody params: DiarySearchParams,
	): RestApiPageResult<ApiDiaryPreview> {
		val userId = user.idNo
		val page = PageRequest.of(params.page, params.pageSize)

		return when {
			params.dates != null -> searchByDates(userId, page, params.dates)
			else -> searchByQuery(userId, page, params.query ?: "")
		}
	}

	private fun searchByQuery(
		userId: String,
		page: Pageable,
		query: String,
	): RestApiPageResult<ApiDiaryPreview> {
		val total = diaryRepository.countByQuery(userId, query)
		val result = diaryRepository.selectByQuery(userId, query, page)
		return RestApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = result.map { it.toApiPreview() },
		)
	}

	private fun searchByDates(
		userId: String,
		page: Pageable,
		dates: List<LocalDate>,
	): RestApiPageResult<ApiDiaryPreview> {
		val total = diaryRepository.countByDates(userId, dates)
		val result = diaryRepository.selectByDates(userId, dates, page)
		return RestApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = result.map { it.toApiPreview() },
		)
	}
}
