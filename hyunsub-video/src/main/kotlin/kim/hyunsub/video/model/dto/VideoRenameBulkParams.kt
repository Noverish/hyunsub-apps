package kim.hyunsub.video.model.dto

data class VideoRenameBulkParams(
	val videoIds: List<String>,
	val from: String,
	val to: String,
	val isRegex: Boolean,
)
