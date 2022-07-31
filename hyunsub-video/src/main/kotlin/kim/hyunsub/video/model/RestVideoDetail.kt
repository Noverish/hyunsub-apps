package kim.hyunsub.video.model

data class RestVideoDetail(
	val videoUrl: String,
	val thumbnailUrl: String,
	val title: String,
	val subtitles: List<RestVideoSubtitle>,
	val metadata: RestVideoMetadata?,
)
