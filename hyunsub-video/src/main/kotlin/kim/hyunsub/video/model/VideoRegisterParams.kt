package kim.hyunsub.video.model

data class VideoRegisterParams(
	val category: String,
	val videoPath: String,
	val outputPath: String,

	val videoEntryId: String?,
	val videoGroupId: String?,
	val videoSeason: String?,
)
