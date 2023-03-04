package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.model.VideoRenameParams
import kim.hyunsub.video.model.VideoRenameResult
import kim.hyunsub.video.model.dto.VideoRenameBulkParams
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
		return videoRenameService.rename(params, renameEntry = true)
	}

	@PostMapping("/bulk")
	fun rename(@RequestBody params: VideoRenameBulkParams): List<VideoRenameResult> {
		return videoRenameService.renameBulk(params)
	}
}
