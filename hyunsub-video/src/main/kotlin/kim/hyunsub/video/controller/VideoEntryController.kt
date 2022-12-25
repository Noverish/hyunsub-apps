package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideoEntry
import kim.hyunsub.video.model.RestVideoEntryDetail
import kim.hyunsub.video.model.VideoSort
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.service.ApiModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import kim.hyunsub.video.service.VideoEntryService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/entry")
class VideoEntryController(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
	private val apiModelConverter: ApiModelConverter,
	private val videoEntryService: VideoEntryService,
) {
	companion object : Log

	@GetMapping("")
	fun list(
		user: UserAuth,
		@RequestParam category: String,
		@RequestParam seed: Int?,
		@RequestParam(required = false, defaultValue = "0") p: Int,
		@RequestParam(required = false, defaultValue = "48") ps: Int,
		@RequestParam(required = false, defaultValue = "random") sort: VideoSort,
	): RestApiPageResult<RestVideoEntry> {
		val availableCategories = videoCategoryService.getAvailableCategories(user)
		if (availableCategories.none { it.name == category }) {
			return RestApiPageResult.empty()
		}

		val total = videoEntryRepository.countByCategory(category)

		val randomSeed = seed ?: System.currentTimeMillis().toInt()

		val sorted = when(sort) {
			VideoSort.random -> videoEntryRepository.findByCategoryOrderByRand(category, randomSeed, PageRequest.of(p, ps))
			VideoSort.abc -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.ASC, "name"))
			VideoSort.zyx -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.DESC, "name"))
			VideoSort.old -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.ASC, "regDt"))
			VideoSort.new -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.DESC, "regDt"))
		}

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = ps,
			data = sorted.map { apiModelConverter.convert(it) },
		)
	}

	@GetMapping("/{entryId}")
	fun detail(
		user: UserAuth,
		@PathVariable entryId: String,
		@RequestParam(required = false) videoId: String?,
	): RestVideoEntryDetail {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		if (!videoEntryService.userHasAuthority(user, entry)) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		return videoEntryService.load(entry, videoId)
	}
}
