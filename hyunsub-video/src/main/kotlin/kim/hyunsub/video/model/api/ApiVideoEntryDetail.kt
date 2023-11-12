package kim.hyunsub.video.model.api

data class ApiVideoEntryDetail(
	val category: ApiVideoCategory,
	val entry: ApiVideoEntry,
	val video: ApiVideo,
	val seasons: List<ApiVideoSeason>? = null,
	val group: ApiVideoGroupDetail? = null,
)
