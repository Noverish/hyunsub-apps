package kim.hyunsub.video.model.api

data class ApiVideo(
	val videoId: String,
	val videoUrl: String,
	val thumbnailUrl: String,
	val title: String,
	val subtitles: List<ApiVideoSubtitle>,
	val metadata: ApiVideoMetadata?,
	val time: Int,
)
