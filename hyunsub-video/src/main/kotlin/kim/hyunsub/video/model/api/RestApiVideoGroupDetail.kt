package kim.hyunsub.video.model.api

import kim.hyunsub.video.model.api.RestApiVideoEntry

data class RestApiVideoGroupDetail(
	val name: String,
	val entries: List<RestApiVideoEntry>,
)
