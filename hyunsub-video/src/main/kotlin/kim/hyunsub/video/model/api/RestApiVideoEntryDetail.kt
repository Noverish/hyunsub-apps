package kim.hyunsub.video.model.api

data class RestApiVideoEntryDetail(
	val category: RestApiVideoCategory,
	val entry: RestApiVideoEntry,
	val video: RestApiVideo,
	val seasons: List<RestApiVideoSeason>? = null,
	val group: RestApiVideoGroupDetail? = null,
)
