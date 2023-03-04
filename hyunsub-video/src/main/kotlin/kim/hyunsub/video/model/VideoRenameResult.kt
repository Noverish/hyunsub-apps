package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle

data class VideoRenameResult(
	val entry: VideoEntry?,
	val video: Video?,
	val subtitles: List<VideoSubtitle>,
	val metadata: VideoMetadata?,
) {
	companion object {
		fun empty() = VideoRenameResult(null, null, emptyList(), null)
	}
}
