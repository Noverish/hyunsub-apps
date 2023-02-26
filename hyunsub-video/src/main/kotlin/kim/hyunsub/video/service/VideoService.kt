package kim.hyunsub.video.service

import kim.hyunsub.video.model.api.RestApiVideo
import kim.hyunsub.video.repository.VideoHistoryRepository
import kim.hyunsub.video.repository.VideoMetadataRepository
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
	private val apiModelConverter: ApiModelConverter,
) {
	fun loadVideo(userId: String, video: Video): RestApiVideo {
		val subtitles = videoSubtitleRepository.findByVideoId(video.id)
		val metadata = videoMetadataRepository.findByIdOrNull(video.path)
		val history = videoHistoryRepository.findByIdOrNull(VideoHistoryId(userId, video.id))
		return apiModelConverter.convertVideo(video, subtitles, metadata, history)
	}
}
