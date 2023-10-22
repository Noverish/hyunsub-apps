package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime

data class VideoMyHistory(
	val videoId: String,
	val time: Int,
	val date: LocalDateTime,
	val entryId: String,
	val duration: Int,
	val thumbnail: String?,
	val path: String,
)
