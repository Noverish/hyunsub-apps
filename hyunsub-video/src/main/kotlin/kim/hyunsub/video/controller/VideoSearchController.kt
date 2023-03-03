package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideoSearchResult
import kim.hyunsub.video.service.ApiModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import kim.hyunsub.video.service.VideoSearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class VideoSearchController(
	private val videoCategoryService: VideoCategoryService,
	private val videoSearchService: VideoSearchService,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun search(
		user: UserAuth,
		q: String,
	): RestVideoSearchResult {
		val categories = videoCategoryService.getAvailableCategories(user).map { it.name }
		if (q.length < 2) {
			throw ErrorCodeException(ErrorCode.SHORT_SEARCH_QUERY)
		}

		val result = videoSearchService.search(categories, q)

		return apiModelConverter.convertVideoSearchResult(result)
	}
}
