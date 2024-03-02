package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.util.toMillis
import kim.hyunsub.common.util.toOdt
import kim.hyunsub.photo.model.PhotoDateType
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class PhotoOwner(
	val userId: String,
	val photoId: String,
	val name: String,
	val fileDt: LocalDateTime,
	val regDt: LocalDateTime,
	val date: LocalDateTime,
	val offset: Int,
	val dateType: PhotoDateType,
	val photoIdNew: String,
) {
	val odt: OffsetDateTime
		get() = date.toOdt(ZoneOffset.ofTotalSeconds(offset))

	val millis: Long
		get() = odt.toMillis()
}
