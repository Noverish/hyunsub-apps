package kim.hyunsub.video.model

data class VideoRegisterParams(
	val type: Int,
	val videoPath: String,
	val videoEntryId: String,
)
