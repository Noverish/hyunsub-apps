package kim.hyunsub.video.model

data class RestVideoEntryDetail(
	val category: String,
	val video: RestApiVideo,
	val episodes: Map<String, List<RestVideoEpisode>>? = null,
	val group: RestVideoGroup? = null,
)
