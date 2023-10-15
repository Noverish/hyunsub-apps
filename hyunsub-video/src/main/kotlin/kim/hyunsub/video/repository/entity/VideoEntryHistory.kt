package kim.hyunsub.video.repository.entity

import java.time.LocalDateTime

interface VideoEntryHistory {
	val entryId: String
	val date: LocalDateTime
	val videoId: String
	val time: Int
	val path: String
	val name: String
	val thumbnail: String
	val category: String
	val duration: Int
}
