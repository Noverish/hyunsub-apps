package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestApiVideoGroupPreview
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.service.ApiModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_video"])
@RestController
@RequestMapping("/api/v1/groups")
class VideoGroupController(
	private val videoGroupRepository: VideoGroupRepository,
	private val videoCategoryService: VideoCategoryService,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun list(user: UserAuth): List<RestApiVideoGroupPreview> {
		val categories = videoCategoryService.getAvailableCategories(user)
		log.debug("[List Video Group] categories={}", categories)
		val categoryIds = categories.map { it.id }
		val groups = videoGroupRepository.findByCategoryIdIn(categoryIds)
		log.debug("[List Video Group] groups={}", groups)
		return groups.map { apiModelConverter.convert(it) }
	}
}
