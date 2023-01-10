package kim.hyunsub.video.model

data class VideoEntryCreateParams(
	val name: String,
	val thumbnail: String?,
	val category: String,
)
