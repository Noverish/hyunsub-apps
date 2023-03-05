package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.photo.model.api.RestApiMyPhotoPreview
import kim.hyunsub.photo.util.PhotoPathUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class MyPhoto(
	val id: String,
	val hash: String,
	val width: Int,
	val height: Int,
	val size: Int,
	val offset: Int,
	val original: String,
	val ext: String,
	val regDt: LocalDateTime,
) {
	fun toPreview() = RestApiMyPhotoPreview(
		id = id,
		thumbnail = thumbnail,
		date = date,
	)

	private val millis: Long
		get() = PhotoV2.restoreMillis(id)

	private val date: OffsetDateTime
		get() = OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.ofTotalSeconds(offset))

	private val year: Int
		get() = date.withOffsetSameInstant(ZoneOffset.UTC).year

	private val thumbnail: String
		get() = FileUrlConverter.convertToUrl(PhotoPathUtils.thumbnail("$id.$ext", year))
}
