package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.VideoEntry

data class VideoSearchResult(
	val entries: List<VideoEntry>,
)
