package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideo
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.service.VideoEntryService
import kim.hyunsub.video.service.VideoService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_video"])
@RestController
@RequestMapping("/api/v1/video")
class VideoController(
	private val videoRepository: VideoRepository,
	private val videoEntryService: VideoEntryService,
	private val videoService: VideoService,
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
}
