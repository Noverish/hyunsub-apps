package kim.hyunsub.diary.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.diary.model.DiarySearchQuery
import kim.hyunsub.diary.repository.DiaryRepository
import kim.hyunsub.diary.repository.entity.Diary
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
	): RestApiPageResult<Diary> {
		val total = diaryRepository.searchCount(user.idNo, params.query)
		val pageRequest = PageRequest.of(params.page, params.pageSize)
		val result = diaryRepository.search(user.idNo, params.query, pageRequest)
		return RestApiPageResult(
			total = total,
			page = params.page,
			pageSize = params.pageSize,
			data = result,
		)
	}
}
