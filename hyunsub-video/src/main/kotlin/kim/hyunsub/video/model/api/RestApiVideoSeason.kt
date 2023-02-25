package kim.hyunsub.video.model.api

data class RestApiVideoSeason(
	val name: String?,
	val episodes: List<RestApiVideoEpisode>,
)
