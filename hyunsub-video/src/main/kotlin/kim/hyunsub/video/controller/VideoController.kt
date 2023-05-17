package kim.hyunsub.video.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.api.RestApiVideo
import kim.hyunsub.video.service.VideoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/videos")
class VideoController(
	private val videoService: VideoService,
) {
	@GetMapping("/{videoId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable videoId: String,
	): RestApiVideo {
		return videoService.loadVideo(userAuth.idNo, videoId)
	}
}
