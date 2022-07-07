package kim.hyunsub.video.controller

import kim.hyunsub.video.service.VideoScanner
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class VideoAdminController(
	private val videoScanner: VideoScanner,
) {
	@PostMapping("/scan")
	fun scan() {
		videoScanner.scan()
	}
}
