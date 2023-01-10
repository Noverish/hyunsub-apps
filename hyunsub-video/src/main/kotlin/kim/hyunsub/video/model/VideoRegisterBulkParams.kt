package kim.hyunsub.video.model

data class VideoRegisterBulkParams(
	val paths: List<String>,
	val entryId: String,
	val videoSeason: String?,
)
