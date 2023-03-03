package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoMetadata

data class VideoRegisterResult(
	val videoEntry: VideoEntry? = null,
	val video: Video? = null,
	val metadata: VideoMetadata,
)
