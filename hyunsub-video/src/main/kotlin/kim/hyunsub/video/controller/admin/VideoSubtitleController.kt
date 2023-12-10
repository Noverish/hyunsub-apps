package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.bo.VideoSubtitleBo
import kim.hyunsub.video.model.api.ApiVideoSubtitle
import kim.hyunsub.video.model.dto.VideoSubtitleParams
import kim.hyunsub.video.model.dto.VideoSubtitleSyncParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/videos/{videoId}/subtitles")
class VideoSubtitleController(
	private val videoSubtitleBo: VideoSubtitleBo,
) {
	@PostMapping("")
	fun upload(
		@PathVariable videoId: String,
		params: VideoSubtitleParams,
	): ApiVideoSubtitle {
		return videoSubtitleBo.upload(videoId, params)
	}

	@PostMapping("/{subtitleId}/sync")
	fun sync(
		@PathVariable videoId: String,
		@PathVariable subtitleId: String,
		@RequestBody params: VideoSubtitleSyncParams,
	): ApiVideoSubtitle {
		return videoSubtitleBo.sync(subtitleId, params)
	}

	@DeleteMapping("/{subtitleId}")
	fun delete(
		@PathVariable videoId: String,
		@PathVariable subtitleId: String,
	): ApiVideoSubtitle {
		return videoSubtitleBo.delete(subtitleId)
	}
}
