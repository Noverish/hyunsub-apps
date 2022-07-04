package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.VideoEntry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/list")
class VideoListController {
	companion object : Log

	@GetMapping("")
	fun list(userAuth: UserAuth): List<VideoEntry> {
		log.debug("list : userAuth={}", userAuth)

		return listOf(
			VideoEntry("/a/b/c", "/a/b/c/thumbnail.jpg"),
			VideoEntry("/e/f/g", "/e/f/g/thumbnail.jpg"),
		)
	}
}
