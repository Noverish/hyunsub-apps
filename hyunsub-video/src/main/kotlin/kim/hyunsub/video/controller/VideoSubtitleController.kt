package kim.hyunsub.video.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import kotlin.io.path.Path
import kotlin.io.path.extension

@Authorized(authorities = ["admin"])
@RestController
@RequestMapping("/api/v1/video/{videoId}/subtitle")
class VideoSubtitleController(
	private val apiCaller: ApiCaller,
	private val videoRepository: VideoRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val randomGenerator: RandomGenerator,
) {
	companion object : Log

	@PostMapping("")
	fun uploadSubtitle(
		@PathVariable videoId: String,
		lang: String,
		@RequestParam(required = false) file: MultipartFile? = null,
		@RequestParam(required = false) path: String? = null,
	): VideoSubtitle {
		log.debug("uploadSubtitle: videoId={}, lang={}, path={}, file={}", videoId, lang, path, file?.originalFilename)

		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val fileExt =
			if (file != null) {
				file.originalFilename
					?.let { Path(it).extension }
					?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
			} else if (path != null) {
				Path(path).extension
			} else {
				throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
			}

		val subtitleExt = fileExt.let { if (lang.isEmpty()) it else "$lang.$it" }
		val subtitlePath = video.path.replace(Regex("mp4$"), subtitleExt)
		log.debug("uploadSubtitle: subtitlePath={}", subtitlePath)

		val subtitles = videoSubtitleRepository.findByVideoId(videoId)
		log.debug("subtitles: subtitles={}", subtitles)
		if (subtitles.any { it.path == subtitlePath }) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		if (file != null) {
			apiCaller.upload(subtitlePath, file.bytes)
		} else if (path != null) {
			apiCaller.rename(path, subtitlePath)
		}

		val videoSubtitle = VideoSubtitle(
			id = randomGenerator.generateRandomString(6),
			path = subtitlePath,
			videoId = videoId,
		)
		log.debug("uploadSubtitle: videoSubtitle={}", videoSubtitle)
		videoSubtitleRepository.save(videoSubtitle)

		return videoSubtitle
	}
}
