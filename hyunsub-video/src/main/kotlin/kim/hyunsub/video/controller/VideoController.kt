package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideo
import kim.hyunsub.video.model.VideoRegisterParams
import kim.hyunsub.video.model.VideoRegisterResult
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.service.VideoEntryService
import kim.hyunsub.video.service.VideoRegisterService
import kim.hyunsub.video.service.VideoService
import kim.hyunsub.video.service.scan.VideoScannerService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/video")
class VideoController(
	private val videoRepository: VideoRepository,
	private val videoEntryService: VideoEntryService,
	private val videoService: VideoService,
	private val videoRegisterService: VideoRegisterService,
) {
	companion object : Log

	@GetMapping("/{videoId}")
	fun detail(
		user: UserAuth,
		@PathVariable videoId: String,
	): RestVideo {
		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		if (!videoEntryService.userHasAuthority(user, video.videoEntryId)) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		return videoService.loadVideo(video)
	}

	@Authorized(authorities = ["admin"])
	@PostMapping("")
	fun register(
		user: UserAuth,
		@RequestBody params: VideoRegisterParams,
	): VideoRegisterResult {
		return videoRegisterService.registerVideo(params)
	}
}
