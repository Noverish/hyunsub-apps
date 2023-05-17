package kim.hyunsub.drive.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.YoutubeDownloadParams
import kim.hyunsub.common.api.model.YoutubeDownloadResult
import kim.hyunsub.common.api.model.YoutubeMetadata
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.drive.service.DrivePathService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/youtube")
class DriveYoutubeController(
	private val apiCaller: ApiCaller,
	private val drivePathService: DrivePathService,
) {
	@GetMapping("/metadata")
	fun formats(userAuth: UserAuth, url: String): YoutubeMetadata {
		return apiCaller.youtubeMetadata(url)
	}

	@PostMapping("/download")
	fun download(userAuth: UserAuth, @RequestBody params: YoutubeDownloadParams): YoutubeDownloadResult {
		val path = drivePathService.getPath(userAuth, params.path)
		return apiCaller.youtubeDownload(params.copy(path = path))
	}
}
