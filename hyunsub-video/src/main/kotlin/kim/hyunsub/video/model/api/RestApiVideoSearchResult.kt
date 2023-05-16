package kim.hyunsub.video.model.api

data class RestApiVideoSearchResult(
	val entries: Map<String, List<RestApiVideoEntry>>,
)
