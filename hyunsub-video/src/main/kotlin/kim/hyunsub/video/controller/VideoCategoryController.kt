package kim.hyunsub.video.controller

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.repository.entity.VideoCategory
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_video"])
@RestController
@RequestMapping("/api/v1/category")
class VideoCategoryController(
	private val videoCategoryService: VideoCategoryService,
) {
	@GetMapping("")
	fun categoryList(userAuth: UserAuth): List<VideoCategory> {
		return videoCategoryService.getAvailableCategories(userAuth.authorityNames)
	}
}
