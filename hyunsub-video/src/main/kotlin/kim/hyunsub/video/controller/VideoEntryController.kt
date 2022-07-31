package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideoEntry
import kim.hyunsub.video.model.VideoSort
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.service.RestModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_video"])
@RestController
@RequestMapping("/api/v1/entry")
class VideoEntryController(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
	private val restModelConverter: RestModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam category: String,
		@RequestParam(required = false, defaultValue = "0") p: Int,
		@RequestParam(required = false, defaultValue = "48") ps: Int,
		@RequestParam(required = false, defaultValue = "random") sort: VideoSort,
	): List<RestVideoEntry> {
		val availableCategories = videoCategoryService.getAvailableCategories(userAuth.authorityNames)
		if (availableCategories.none { it.name == category }) {
			return emptyList()
		}

		val sorted = when(sort) {
			VideoSort.random -> videoEntryRepository.findByCategoryOrderByRand(category, PageRequest.of(p, ps))
			VideoSort.abc -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.ASC, "name"))
			VideoSort.zyx -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.DESC, "name"))
			VideoSort.old -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.ASC, "regDt"))
			VideoSort.new -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.DESC, "regDt"))
		}

		return sorted.map { restModelConverter.convertVideoEntry(it) }
	}
}
