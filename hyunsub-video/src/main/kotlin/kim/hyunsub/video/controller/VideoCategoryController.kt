package kim.hyunsub.video.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.api.ApiVideoCategory
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
class VideoCategoryController(
	private val videoCategoryService: VideoCategoryService,
) {
	@GetMapping("")
	fun list(user: UserAuth): List<ApiVideoCategory> {
		return videoCategoryService.getAvailableCategories(user)
			.map { ApiVideoCategory(it) }
	}
}
