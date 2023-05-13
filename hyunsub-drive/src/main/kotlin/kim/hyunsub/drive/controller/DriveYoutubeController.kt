package kim.hyunsub.drive.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.YoutubeFormat
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/youtube")
class DriveYoutubeController(
	private val apiCaller: ApiCaller,
) {
	@GetMapping("/formats")
	fun formats(userAuth: UserAuth, url: String): List<YoutubeFormat> {
		return apiCaller.youtubeFormats(url)
	}
}
