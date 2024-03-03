package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.util.toLdt
import kim.hyunsub.photo.model.PhotoDateType
import java.time.LocalDateTime

data class ApiPhotoMetadata(
	val photoId: String,
	val userId: String,

	val name: String,
	val fileEpoch: Int,
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
) {
	val fileDt: LocalDateTime
		get() = (fileEpoch * 1000).toLong().toLdt()
}
