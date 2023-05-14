package kim.hyunsub.video.model.dto

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoHistory
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle

data class VideoDeleteResult(
	val video: Video,
	val subtitles: List<VideoSubtitle>,
	val metadata: VideoMetadata?,
	val histories: List<VideoHistory>,
)
