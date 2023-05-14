package kim.hyunsub.video.model.dto

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle

data class VideoScanResult(
	val video: Video,
	val metadata: VideoMetadata,
	val subtitles: List<VideoSubtitle>,
)
