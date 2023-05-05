package kim.hyunsub.video.model.dto

data class VideoEntryCreateParams(
	val name: String,
	val thumbnailUrl: String?,
	val category: String,
	val videoGroupId: String?,
)
