package kim.hyunsub.photo.model

import java.time.LocalDateTime

data class RestApiExifDate(
	val photoId: Int,
	val name: String,
	val date: LocalDateTime,

	val nameDate: LocalDateTime?,
	val dateTimeCreated: LocalDateTime?,
	val subSecCreateDate: LocalDateTime?,
	val fileModifyDate: LocalDateTime?,
	val modifyDate: LocalDateTime?,
	val createDate: LocalDateTime?,
	val creationDate: LocalDateTime?,
	val dateTimeOriginal: LocalDateTime?,
	val timeStamp: LocalDateTime?,
	val gpsDateTime: LocalDateTime?,
)
