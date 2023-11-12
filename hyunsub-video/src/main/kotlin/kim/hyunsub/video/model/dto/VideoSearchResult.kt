package kim.hyunsub.video.model.dto

import kim.hyunsub.video.model.api.ApiVideoEntry

data class VideoSearchResult(
	val entries: Map<String, List<ApiVideoEntry>>,
)
