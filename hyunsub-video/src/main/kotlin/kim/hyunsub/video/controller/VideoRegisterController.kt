package kim.hyunsub.video.controller

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.VideoRegisterBulkParams
import kim.hyunsub.video.model.VideoRegisterBulkResult
import kim.hyunsub.video.model.VideoRegisterParams
import kim.hyunsub.video.model.VideoRegisterResult
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.service.VideoEntryService
import kim.hyunsub.video.service.VideoRegisterService
import kim.hyunsub.video.service.VideoService
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

	@PostMapping("/bulk")
	fun registerBulk(
		user: UserAuth,
		@RequestBody params: VideoRegisterBulkParams,
	): VideoRegisterBulkResult {
		return videoRegisterService.registerVideoBulk(params)
	}
}
