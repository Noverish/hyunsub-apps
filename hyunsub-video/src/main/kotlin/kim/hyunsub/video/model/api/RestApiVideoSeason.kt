package kim.hyunsub.video.model.api

import kim.hyunsub.video.repository.entity.VideoEpisode

data class RestApiVideoSeason(
	val name: String?,
	val episodes: List<VideoEpisode>,
)
