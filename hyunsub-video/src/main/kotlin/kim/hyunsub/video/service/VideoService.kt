package kim.hyunsub.video.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.api.ApiVideo
import kim.hyunsub.video.model.dto.VideoDeleteResult
import kim.hyunsub.video.repository.VideoHistoryRepository
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoHistoryId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class VideoService(
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
	private val videoHistoryRepository: VideoHistoryRepository,
	private val videoRepository: VideoRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	fun loadVideo(userId: String, videoId: String): ApiVideo {
		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		return loadVideo(userId, video)
	}

	fun loadVideo(userId: String, video: Video): ApiVideo {
		val subtitles = videoSubtitleRepository.findByVideoId(video.id)
		val metadata = videoMetadataRepository.findByIdOrNull(video.path)
		val history = videoHistoryRepository.findByIdOrNull(VideoHistoryId(userId, video.id))
		return apiModelConverter.convertVideo(video, subtitles, metadata, history)
	}

	fun delete(video: Video): VideoDeleteResult {
		val histories = videoHistoryRepository.findByVideoId(video.id)
		val metadata = videoMetadataRepository.findByIdOrNull(video.path)
		val subtitles = videoSubtitleRepository.findByVideoId(video.id)

		videoHistoryRepository.deleteAll(histories)
		metadata?.let { videoMetadataRepository.delete(it) }
		videoSubtitleRepository.deleteAll(subtitles)
		videoRepository.delete(video)

		return VideoDeleteResult(video, subtitles, metadata, histories)
	}
}
