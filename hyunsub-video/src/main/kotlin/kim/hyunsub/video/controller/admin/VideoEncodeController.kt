package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.video.model.VideoEncodeParams
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.service.VideoEncodeApiCaller
import kim.hyunsub.video.service.VideoMetadataService
import net.bytebuddy.build.Plugin.Factory.Simple
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/encode")
class VideoEncodeController(
	private val encodeApiCaller: VideoEncodeApiCaller,
	private val videoRepository: VideoRepository,
) {
	companion object : Log

	@PostMapping("")
	fun encode(@RequestBody params: VideoEncodeParams): SimpleResponse {
		val video = videoRepository.findByIdOrNull(params.videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		log.debug("[Encode] params={}", params)

		encodeApiCaller.encode(video, params.options)

		return SimpleResponse()
	}
}
