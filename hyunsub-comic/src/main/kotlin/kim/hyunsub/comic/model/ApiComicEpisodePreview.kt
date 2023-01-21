package kim.hyunsub.comic.model

import java.time.LocalDateTime

data class ApiComicEpisodePreview(
	val order: Int,
	val title: String,
	val length: Int,
	val regDt: LocalDateTime,
)
