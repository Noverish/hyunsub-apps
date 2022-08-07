package kim.hyunsub.video.model

data class RestVideoEntryDetail(
	val video: RestVideo,
	val episodes: Map<String, List<RestVideoEpisode>>? = null,
	val group: RestVideoGroup? = null,
)
