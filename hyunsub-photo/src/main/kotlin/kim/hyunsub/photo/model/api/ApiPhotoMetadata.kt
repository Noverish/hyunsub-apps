package kim.hyunsub.photo.model.api

import com.fasterxml.jackson.annotation.JsonIgnore
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.repository.entity.Photo
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class ApiPhotoMetadata(
	val photoId: String,
	@JsonIgnore
	val offset: Int,
	val dateType: PhotoDateType,
	val userId: String,
	val name: String,
	val fileDt: LocalDateTime,
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
	val date: OffsetDateTime
		get() = Photo.parseDate(photoId, offset)
}
