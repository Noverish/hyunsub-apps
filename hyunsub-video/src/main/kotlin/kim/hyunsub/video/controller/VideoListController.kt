package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_video"])
@RestController
@RequestMapping("/api/v1/list")
class VideoListController(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
) {
	companion object : Log

	@GetMapping("/{category}")
	fun list(
		userAuth: UserAuth,
		@PathVariable category: String,
		@RequestParam p: Int = 0,
	): List<VideoEntry> {
		val availableCategories = videoCategoryService.getAvailableCategories(userAuth.authorityNames)
		if (availableCategories.none { it.name == category }) {
			return emptyList()
		}

		val pageable = PageRequest.of(p, 50)
		return videoEntryRepository.findByCategory(category, pageable)
	}
}
