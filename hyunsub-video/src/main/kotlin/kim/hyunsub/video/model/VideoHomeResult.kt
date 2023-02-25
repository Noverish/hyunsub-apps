package kim.hyunsub.video.model

import kim.hyunsub.video.model.api.RestApiVideoCategory
import kim.hyunsub.video.model.api.RestApiVideoEntry

data class VideoHomeResult(
	val recents: List<VideoHomeRecent>,
)

data class VideoHomeRecent(
	val category: RestApiVideoCategory,
	val list: List<RestApiVideoEntry>,
)
