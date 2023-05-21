package kim.hyunsub.drive.controller

import kim.hyunsub.common.fs.FsVideoClient
import kim.hyunsub.common.fs.model.YoutubeDownloadParams
import kim.hyunsub.common.fs.model.YoutubeDownloadResult
import kim.hyunsub.common.fs.model.YoutubeMetadata
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
	private val fsVideoClient: FsVideoClient,
	private val drivePathService: DrivePathService,
) {
	@GetMapping("/metadata")
	fun formats(userAuth: UserAuth, url: String): YoutubeMetadata {
		return fsVideoClient.youtubeMetadata(url)
	}

	@PostMapping("/download")
	fun download(userAuth: UserAuth, @RequestBody params: YoutubeDownloadParams): YoutubeDownloadResult {
		val path = drivePathService.getPath(userAuth, params.path)
		return fsVideoClient.youtubeDownload(params.copy(path = path))
	}
}
