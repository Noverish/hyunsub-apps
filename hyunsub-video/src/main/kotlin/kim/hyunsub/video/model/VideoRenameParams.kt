package kim.hyunsub.video.model

data class VideoRenameParams(
	val videoId: String,
	val from: String,
	val to: String,
	val isRegex: Boolean,
)
