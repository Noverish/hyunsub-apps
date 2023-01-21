package kim.hyunsub.comic.model

import java.time.LocalDateTime

data class ApiComicEpisodeDetail(
	val comicId: String,
	val order: Int,
	val title: String,
	val length: Int,
	val regDt: LocalDateTime,
	val images: List<String>,
)
