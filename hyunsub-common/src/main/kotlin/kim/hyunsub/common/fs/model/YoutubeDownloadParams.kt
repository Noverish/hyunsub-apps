package kim.hyunsub.common.fs.model

data class YoutubeDownloadParams(
	val url: String,
	val path: String,
	val resolution: String,
	val subtitles: List<String>,
)
