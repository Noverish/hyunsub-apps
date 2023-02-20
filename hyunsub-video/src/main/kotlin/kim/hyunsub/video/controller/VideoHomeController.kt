package kim.hyunsub.video.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.dto.VideoHomeRecent
import kim.hyunsub.video.model.dto.VideoHomeResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.service.ApiModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/home")
class VideoHomeController(
	private val videoCategoryService: VideoCategoryService,
	private val videoEntryRepository: VideoEntryRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	@GetMapping("")
	fun home(
		userAuth: UserAuth,
	): VideoHomeResult {
		val categories = videoCategoryService.getAvailableCategories(userAuth)

		val recents = categories.map { category ->
			VideoHomeRecent(
				category = category.toDto(),
				list = videoEntryRepository.findByCategoryOrderByRegDtDesc(category.name, PageRequest.of(0, 12))
					.map { it.toDto() }
			)
		}

		return VideoHomeResult(
			recents = recents,
		)
	}
}
