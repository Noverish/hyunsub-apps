package kim.hyunsub.video.bo

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsSubtitleClient
import kim.hyunsub.common.fs.client.FsUploadBinaryClient
import kim.hyunsub.common.fs.client.remove
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.fs.model.SubtitleSyncParams
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.api.ApiVideoSubtitle
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.model.dto.VideoSubtitleParams
import kim.hyunsub.video.model.dto.VideoSubtitleSyncParams
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.VideoSubtitle
import kim.hyunsub.video.repository.generateId
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class VideoSubtitleBo(
	private val fsClient: FsClient,
	private val fsUploadClient: FsUploadBinaryClient,
	private val fsSubtitleClient: FsSubtitleClient,
	private val videoRepository: VideoRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
) {
	private val log = KotlinLogging.logger { }

	fun upload(videoId: String, params: VideoSubtitleParams): ApiVideoSubtitle {
		val (lang, file, path, override) = params

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
			fsUploadClient.binary(subtitlePath, file.bytes, override)
		} else if (path != null && path != subtitlePath) {
			fsClient.rename(path, subtitlePath, override)
		}

		if (subtitle != null) {
			return subtitle.toApi()
		}

		val videoSubtitle = VideoSubtitle(
			id = videoSubtitleRepository.generateId(),
			path = subtitlePath,
			videoId = videoId,
		)
		log.debug("[Upload Video Subtitle] new subtitle={}", videoSubtitle)
		videoSubtitleRepository.save(videoSubtitle)

		return videoSubtitle.toApi()
	}

	fun sync(subtitleId: String, params: VideoSubtitleSyncParams): ApiVideoSubtitle {
		val subtitle = videoSubtitleRepository.findByIdOrNull(subtitleId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such subtitle")

		if (!subtitle.path.endsWith(".srt")) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "This is not srt")
		}

		fsSubtitleClient.sync(SubtitleSyncParams(subtitle.path, params.ms))

		return subtitle.toApi()
	}

	fun delete(subtitleId: String): ApiVideoSubtitle {
		val subtitle = videoSubtitleRepository.findByIdOrNull(subtitleId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such subtitle")

		videoSubtitleRepository.delete(subtitle)

		fsClient.remove(subtitle.path)

		return subtitle.toApi()
	}
}