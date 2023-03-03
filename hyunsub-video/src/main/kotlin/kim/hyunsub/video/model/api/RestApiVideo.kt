package kim.hyunsub.video.model.api

data class RestApiVideo(
	val videoId: String,
	val videoUrl: String,
	val thumbnailUrl: String,
	val title: String,
	val subtitles: List<RestApiVideoSubtitle>,
	val metadata: RestApiVideoMetadata?,
	val time: Int,
)
