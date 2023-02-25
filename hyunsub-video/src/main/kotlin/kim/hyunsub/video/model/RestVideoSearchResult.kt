package kim.hyunsub.video.model

import kim.hyunsub.video.model.api.RestApiVideoEntry

data class RestVideoSearchResult(
	val entries: Map<String, List<RestApiVideoEntry>>,
)
