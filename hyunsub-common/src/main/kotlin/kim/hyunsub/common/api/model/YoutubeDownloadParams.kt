package kim.hyunsub.common.api.model

data class YoutubeDownloadParams(
	val url: String,
	val path: String,
	val resolution: String,
	val subtitles: List<String>,
)
