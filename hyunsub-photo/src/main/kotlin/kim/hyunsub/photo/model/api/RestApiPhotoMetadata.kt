package kim.hyunsub.photo.model.api

import com.fasterxml.jackson.annotation.JsonIgnore
import kim.hyunsub.photo.repository.entity.PhotoV2
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class RestApiPhotoMetadata(
	val photoId: String,
	@JsonIgnore
	val offset: Int,
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
		get() = PhotoV2.parseDate(photoId, offset)
}
