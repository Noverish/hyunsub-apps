package kim.hyunsub.video.model.dto

data class VideoRegisterParams(
	val category: String?,
	val videoPath: String,
	val outputPath: String,
	val videoGroupId: String?,
	val newGroupName: String?,
	val thumbnailUrl: String?,

	val videoEntryId: String?,
	val videoSeason: String?,
)
