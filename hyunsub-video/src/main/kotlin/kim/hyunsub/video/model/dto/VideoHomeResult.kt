package kim.hyunsub.video.model.dto

import kim.hyunsub.video.model.api.ApiVideoCategory
import kim.hyunsub.video.model.api.ApiVideoEntry
import kim.hyunsub.video.model.api.ApiVideoEntryHistory

data class VideoHomeResult(
	val recents: List<VideoHomeRecent>,
	val histories: List<ApiVideoEntryHistory>,
)

data class VideoHomeRecent(
	val category: ApiVideoCategory,
	val list: List<ApiVideoEntry>,
)
