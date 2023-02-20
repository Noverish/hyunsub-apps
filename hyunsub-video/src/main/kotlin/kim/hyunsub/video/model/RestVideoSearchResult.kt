package kim.hyunsub.video.model

data class RestVideoSearchResult(
	val entries: Map<String, List<RestApiVideoEntry>>,
)
