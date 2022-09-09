package kim.hyunsub.photo.model

import java.time.LocalDateTime

data class RestApiPhoto(
	val id: Int,
	val thumbnail: String,
	val url: String,
	val width: Int,
	val height: Int,
	val date: LocalDateTime,
	val size: Int,
)
