package kim.hyunsub.video.model.dto

data class VideoEntryCreateParams(
	val name: String,
	val thumbnail: String?,
	val category: String,
	val videoGroupId: String?,
)
