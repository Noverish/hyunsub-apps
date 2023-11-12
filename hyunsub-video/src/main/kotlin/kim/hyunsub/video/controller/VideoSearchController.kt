package kim.hyunsub.video.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.bo.VideoSearchBo
import kim.hyunsub.video.model.dto.VideoSearchResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search")
class VideoSearchController(
	private val videoSearchBo: VideoSearchBo,
) {
	@GetMapping("")
	fun search(
		user: UserAuth,
		q: String,
	): VideoSearchResult {
		return videoSearchBo.search(user, q)
	}
}
