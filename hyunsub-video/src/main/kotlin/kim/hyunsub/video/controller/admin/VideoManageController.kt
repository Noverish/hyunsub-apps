package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.fs.FsVideoClient
import kim.hyunsub.common.fs.model.VideoThumbnailResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.video.model.dto.VideoEncodeParams
import kim.hyunsub.video.model.dto.VideoSubtitleParams
import kim.hyunsub.video.model.dto.VideoThumbnailParams
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle
import kim.hyunsub.video.service.VideoEncodeApiCaller
import kim.hyunsub.video.service.VideoMetadataService
import kim.hyunsub.video.service.VideoSubtitleService
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/videos/{videoId}/manage")
class VideoManageController(
	private val encodeApiCaller: VideoEncodeApiCaller,
	private val videoRepository: VideoRepository,
	private val videoMetadataService: VideoMetadataService,
	private val videoSubtitleService: VideoSubtitleService,
	private val fsVideoClient: FsVideoClient,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/encode")
	fun encode(
		@PathVariable videoId: String,
		@RequestBody params: VideoEncodeParams,
	): SimpleResponse {
		log.debug { "[Video Encode] videoId=$videoId, params=$params" }

		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		encodeApiCaller.encode(video, params.options)

		return SimpleResponse()
	}

	@PostMapping("/metadata")
	fun metadata(
		@PathVariable videoId: String,
	): VideoMetadata {
		log.debug { "[Video Metadata] videoId=$videoId" }
		return videoMetadataService.scanAndSave(videoId)
	}

	@PostMapping("/thumbnail")
	fun thumbnail(
		@PathVariable videoId: String,
		@RequestBody params: VideoThumbnailParams,
	): VideoThumbnailResult {
		log.debug { "[Video Thumbnail] videoId=$videoId, params=$params" }

		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val apiParams = kim.hyunsub.common.fs.model.VideoThumbnailParams(
			input = video.path,
			time = params.time,
		)
		return fsVideoClient.videoThumbnail(apiParams)
	}

	@PostMapping("/subtitle")
	fun subtitle(
		@PathVariable videoId: String,
		params: VideoSubtitleParams,
	): VideoSubtitle {
		log.debug { "[Video Subtitle] videoId=$videoId, params=$params" }
		return videoSubtitleService.uploadSubtitle(videoId, params)
	}
}
