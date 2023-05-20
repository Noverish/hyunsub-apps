package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toByteArray
import kim.hyunsub.common.util.toHex
import kim.hyunsub.common.util.toLong
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.model.PhotoType
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.util.PhotoPathConverter
import kim.hyunsub.photo.util.isVideo
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo")
data class Photo(
	@Id
	val id: String,

	@Column(nullable = false)
	val hash: String,

	@Column(nullable = false)
	val width: Int,

	@Column(nullable = false)
	val height: Int,

	@Column(nullable = false)
	val size: Int,

	@Column(nullable = false)
	val offset: Int,

	@Column(nullable = false)
	val ext: String,

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	val dateType: PhotoDateType,

	@Column
	val pairPhotoId: String? = null,
) {
	companion object {
		fun generateId(millis: Long, hash: String, i: Int = 0): String {
			val hex = millis.toByteArray().toHex()
			val subHex = hex.substring(5)
			val subHash = hash.substring(i, i + 5)
			return "$subHex$subHash"
		}

		fun restoreMillis(id: String): Long {
			val subHex = id.substring(0, 11)
			val base64 = "00000$subHex"
			return base64.decodeHex().toLong()
		}

		fun parseYear(id: String): Int {
			val millis = restoreMillis(id)
			return Instant.ofEpochMilli(millis).atOffset(ZoneOffset.UTC).year
		}

		fun parseDate(id: String, offset: Int): OffsetDateTime =
			OffsetDateTime.ofInstant(Instant.ofEpochMilli(restoreMillis(id)), ZoneOffset.ofTotalSeconds(offset))
	}

	fun toPreview() = ApiPhotoPreview(
		id = id,
		thumbnail = thumbnail,
		date = date,
		type = if (isVideo) PhotoType.VIDEO else PhotoType.PHOTO,
		ext = ext,
	)

	val millis: Long
		get() = restoreMillis(id)

	val date: OffsetDateTime
		get() = parseDate(id, offset)

	val year: Int
		get() = date.withOffsetSameInstant(ZoneOffset.UTC).year

	private val thumbnail: String
		get() = FileUrlConverter.convertToUrl(PhotoPathConverter.thumbnail(this.id))

	val fileName: String
		get() = "$id.$ext"

	val isVideo: Boolean
		get() = isVideo(fileName)
}
