package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.VideoRenameHistory

data class VideoRenameParams(
	val videoId: String,
	val from: String,
	val to: String,
	val isRegex: Boolean,
) {
	fun toEntity() = VideoRenameHistory(
		videoId = videoId,
		from = from,
		to = to,
		isRegex = isRegex
	)
}
