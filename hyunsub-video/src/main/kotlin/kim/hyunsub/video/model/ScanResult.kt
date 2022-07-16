package kim.hyunsub.video.model

import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoGroup
import kim.hyunsub.video.repository.entity.VideoSubtitle

data class ScanResult(
	val videoGroups: List<VideoGroup>,
	val videoEntries: List<VideoEntry>,
	val videos: List<Video>,
	val subtitles: List<VideoSubtitle>,
)
