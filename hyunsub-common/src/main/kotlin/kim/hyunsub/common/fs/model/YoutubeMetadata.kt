package kim.hyunsub.common.fs.model

data class YoutubeMetadata(
	val resolutions: List<String>,
	val subtitles: List<YoutubeMetadataSubtitle>,
)

data class YoutubeMetadataSubtitle(
	val lang: String,
	val label: String,
)
