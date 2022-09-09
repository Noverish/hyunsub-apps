package kim.hyunsub.video.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.VideoThumbnailResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.RestScanParams
import kim.hyunsub.video.model.ScanResult
import kim.hyunsub.video.model.VideoThumbnailParams
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.service.scan.VideoScannerService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
@Authorized(authorities = ["admin"])
class VideoAdminController(
	private val videoScannerService: VideoScannerService,
	private val videoRepository: VideoRepository,
	private val apiCaller: ApiCaller,
) {
	@PostMapping("/scan")
	fun scan(@RequestBody params: RestScanParams): ScanResult {
		return videoScannerService.scan(params)
	}

	@PostMapping("/video-thumbnail")
	fun videoThumbnail(@RequestBody params: VideoThumbnailParams): VideoThumbnailResult {
		val video = videoRepository.findByIdOrNull(params.videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val apiParams = kim.hyunsub.common.api.model.VideoThumbnailParams(
			input = video.path,
			time = params.time,
		)
		return apiCaller.videoThumbnail(apiParams)
	}
}
