package kim.hyunsub.video.controller

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestApiVideoCategory
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
	fun list(user: UserAuth): List<RestApiVideoCategory> {
		return videoCategoryService.getAvailableCategories(user)
			.map { RestApiVideoCategory(it) }
	}
}
