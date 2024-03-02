package kim.hyunsub.photo.repository.entity

import kim.hyunsub.photo.model.PhotoDateType
import java.time.LocalDateTime

data class ApiPhotoMetadata(
	val photoId: String,
	val userId: String,

	val name: String,
	val fileDt: LocalDateTime,
	val date: LocalDateTime,
	val offset: Int,
	val dateType: PhotoDateType,

	val subSecDateTimeOriginal: String?,
	val dateTimeOriginal: String?,
	val gpsDateTime: String?,
	val timeStamp: String?,
	val modifyDate: String?,
	val creationDate: String?,
	val offsetTime: String?,
	val offsetTimeOriginal: String?,
	val offsetTimeDigitized: String?,
)
