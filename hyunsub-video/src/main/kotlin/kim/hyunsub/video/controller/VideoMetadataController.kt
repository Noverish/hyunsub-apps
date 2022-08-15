package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.model.VideoMetadataScanParams
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.service.VideoMetadataService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["admin"])
@RestController
@RequestMapping("/api/v1/metadata")
class VideoMetadataController(
	private val videoMetadataService: VideoMetadataService,
) {
	companion object : Log

	@PostMapping("/scan")
	fun scan(
		@RequestBody params: VideoMetadataScanParams,
	): VideoMetadata {
		return videoMetadataService.scanAndSave(params.videoId)
	}
}
