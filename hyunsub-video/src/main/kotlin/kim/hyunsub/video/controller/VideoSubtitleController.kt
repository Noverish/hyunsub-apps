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
		@RequestParam(required = false) file: MultipartFile?,
		@RequestParam(required = false) path: String?,
		@RequestParam(required = false) override: Boolean = false,
	): VideoSubtitle {
		log.debug("[Upload Video Subtitle] videoId={}, lang={}, override={}, path={}, file={}", videoId, lang, override, path, file?.originalFilename)

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
		log.debug("[Upload Video Subtitle] subtitlePath={}", subtitlePath)

		val subtitle = videoSubtitleRepository.findByVideoId(videoId).firstOrNull { it.path == subtitlePath }
		log.debug("[Upload Video Subtitle] exist subtitle={}", subtitle)

		if (!override && subtitle != null) {
			throw ErrorCodeException(ErrorCode.ALREADY_EXIST)
		}

		if (file != null) {
			apiCaller.upload(subtitlePath, file.bytes, override)
		} else if (path != null) {
			apiCaller.rename(path, subtitlePath, override)
		}

		if (subtitle != null) {
			return subtitle
		}

		val videoSubtitle = VideoSubtitle(
			id = VideoSubtitle.generateId(videoSubtitleRepository, randomGenerator),
			path = subtitlePath,
			videoId = videoId,
		)
		log.debug("[Upload Video Subtitle] new subtitle={}", videoSubtitle)
		videoSubtitleRepository.save(videoSubtitle)

		return videoSubtitle
	}
}
