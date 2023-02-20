package kim.hyunsub.video.model.dto

import kim.hyunsub.video.model.RestApiVideoCategory
import kim.hyunsub.video.model.RestApiVideoEntry

data class VideoHomeResult(
	val recents: List<VideoHomeRecent>,
)

data class VideoHomeRecent(
	val category: RestApiVideoCategory,
	val list: List<RestApiVideoEntry>,
)
