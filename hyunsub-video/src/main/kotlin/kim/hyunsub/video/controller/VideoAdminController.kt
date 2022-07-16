package kim.hyunsub.video.controller

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.model.RestScanParams
import kim.hyunsub.video.model.ScanResult
import kim.hyunsub.video.service.scan.VideoScannerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
@Authorized(authorities = ["admin"])
class VideoAdminController(
	private val videoScannerService: VideoScannerService,
) {
	@PostMapping("/scan")
	fun scan(@RequestBody params: RestScanParams): ScanResult {
		return videoScannerService.scan(params)
	}
}
