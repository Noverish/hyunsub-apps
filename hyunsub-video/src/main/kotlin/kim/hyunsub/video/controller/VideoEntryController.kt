package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideoEntry
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.service.RestModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.data.domain.PageRequest
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
		@RequestParam p: Int = 0,
		@RequestParam(required = false) ps: Int = 48,
	): List<RestVideoEntry> {
		val availableCategories = videoCategoryService.getAvailableCategories(userAuth.authorityNames)
		if (availableCategories.none { it.name == category }) {
			return emptyList()
		}

		val pageable = PageRequest.of(p, ps)
		return videoEntryRepository.findByCategory(category, pageable)
			.map { restModelConverter.convertVideoEntry(it) }
	}
}
