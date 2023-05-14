package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.dto.VideoScanParams
import kim.hyunsub.video.model.dto.VideoScanResult
import kim.hyunsub.video.service.VideoScanService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/scan")
class VideoScanController(
	private val videoScanService: VideoScanService,
) {
	@PostMapping("")
	fun scan(
		user: UserAuth,
		@RequestBody params: VideoScanParams,
	): List<VideoScanResult> {
		return videoScanService.scan(params)
	}
}
