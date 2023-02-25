package kim.hyunsub.video.controller.callback

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.video.service.VideoMetadataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/encode/callback")
class VideoEncodeCallbackController(
	private val videoMetadataService: VideoMetadataService,
) {
	companion object : Log

	@GetMapping("")
	fun callback(@RequestParam videoId: String): SimpleResponse {
		log.info("[Encode Callback] videoId={}", videoId)

		videoMetadataService.scanAndSave(videoId)
		return SimpleResponse()
	}
}
