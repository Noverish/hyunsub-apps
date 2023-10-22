package kim.hyunsub.diary.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.model.DiarySearchQuery
import kim.hyunsub.diary.model.api.ApiDiaryPreview
import kim.hyunsub.diary.model.api.toApiPreview
import kim.hyunsub.diary.repository.DiaryRepository
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class DiarySearchController(
	private val diaryRepository: DiaryRepository,
) {
	@PostMapping("")
	fun search(
		user: UserAuth,
		@RequestBody params: DiarySearchQuery,
	): RestApiPageResult<ApiDiaryPreview> {
		val total = diaryRepository.searchCount(user.idNo, params.query ?: "")
		val pageRequest = PageRequest.of(params.page, params.pageSize)
		val result = diaryRepository.search(user.idNo, params.query ?: "", pageRequest)
		return RestApiPageResult(
			total = total,
			page = params.page,
			pageSize = params.pageSize,
			data = result.map { it.toApiPreview() },
		)
	}
}
