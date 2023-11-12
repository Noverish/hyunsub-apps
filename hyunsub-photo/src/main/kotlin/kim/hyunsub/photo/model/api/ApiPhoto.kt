package kim.hyunsub.photo.model.api

import kim.hyunsub.photo.model.PhotoDateType
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class ApiPhoto(
	val id: String,
	val imageSize: String,
	val fileSize: String,
	val date: OffsetDateTime,
	val fileName: String,
	val regDt: LocalDateTime,
	val dateType: PhotoDateType,
	val original: String,
)
