package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.dto.VideoRegisterParams
import kim.hyunsub.video.model.dto.VideoRegisterResult
import kim.hyunsub.video.service.VideoRegisterService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/register")
class VideoRegisterController(
	private val videoRegisterService: VideoRegisterService,
) {
	val log = KotlinLogging.logger { }

	@PostMapping("")
	fun register(
		user: UserAuth,
		@RequestBody params: VideoRegisterParams,
	): VideoRegisterResult {
		return videoRegisterService.registerVideo(params)
	}
}
