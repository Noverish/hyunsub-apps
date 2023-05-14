package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiSimpleResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.VideoThumbnailParams
import kim.hyunsub.video.repository.VideoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin")
class VideoAdminController(
	private val videoRepository: VideoRepository,
	private val apiCaller: ApiCaller,
) {
	@PostMapping("/video-thumbnail")
	fun videoThumbnail(@RequestBody params: VideoThumbnailParams): ApiSimpleResult {
		val video = videoRepository.findByIdOrNull(params.videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val apiParams = kim.hyunsub.common.api.model.VideoThumbnailParams(
			input = video.path,
			time = params.time,
		)
		return apiCaller.videoThumbnail(apiParams)
	}
}
