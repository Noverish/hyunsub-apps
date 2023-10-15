package kim.hyunsub.video.model.dto

import kim.hyunsub.video.model.api.ApiVideoEntryHistory
import kim.hyunsub.video.model.api.RestApiVideoCategory
import kim.hyunsub.video.model.api.RestApiVideoEntry

data class VideoHomeResult(
	val recents: List<VideoHomeRecent>,
	val histories: List<ApiVideoEntryHistory>,
)

data class VideoHomeRecent(
	val category: RestApiVideoCategory,
	val list: List<RestApiVideoEntry>,
)
