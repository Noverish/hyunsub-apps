package kim.hyunsub.video.model.api

data class RestApiVideoGroupDetail(
	val name: String,
	val entries: List<RestApiVideoEntry>,
)
