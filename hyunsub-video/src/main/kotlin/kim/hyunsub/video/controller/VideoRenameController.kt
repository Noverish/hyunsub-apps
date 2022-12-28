package kim.hyunsub.video.controller

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.model.VideoRenameParams
import kim.hyunsub.video.model.VideoRenameResult
import kim.hyunsub.video.service.VideoRenameService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/rename")
class VideoRenameController(
	private val videoRenameService: VideoRenameService,
) {
	@PostMapping("")
	fun rename(@RequestBody params: VideoRenameParams): VideoRenameResult {
		return videoRenameService.rename(params)
	}
}
