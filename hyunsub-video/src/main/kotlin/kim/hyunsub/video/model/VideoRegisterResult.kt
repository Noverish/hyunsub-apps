package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoSubtitle

data class VideoRegisterResult(
	val video: Video,
	val subtitles: List<VideoSubtitle>,
)
