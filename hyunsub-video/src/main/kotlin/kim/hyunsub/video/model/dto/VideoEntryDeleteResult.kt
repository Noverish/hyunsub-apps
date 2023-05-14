package kim.hyunsub.video.model.dto

import kim.hyunsub.video.repository.entity.VideoEntry

data class VideoEntryDeleteResult(
	val entry: VideoEntry,
	val videos: List<VideoDeleteResult>,
)
