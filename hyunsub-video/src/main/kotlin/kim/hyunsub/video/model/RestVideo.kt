package kim.hyunsub.video.model

data class RestVideo (
	val videoId: String,
	val videoUrl: String,
	val thumbnailUrl: String,
	val title: String,
	val subtitles: List<RestVideoSubtitle>,
	val metadata: RestVideoMetadata?,
)
