package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.util.decodeHex
import kim.hyunsub.common.util.toByteArray
import kim.hyunsub.common.util.toHex
import kim.hyunsub.common.util.toLong
import kim.hyunsub.common.util.toOdt
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.util.isVideo
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class Photo(
	val id: String,
	val hash: String,
	val width: Int,
	val height: Int,
	val size: Int,
	val offset: Int,
	val ext: String,
	val dateType: PhotoDateType,
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
			return millis.toOdt(ZoneOffset.UTC).year
		}

		fun parseDate(id: String, offset: Int): OffsetDateTime =
			restoreMillis(id).toOdt(ZoneOffset.ofTotalSeconds(offset))
	}

	val millis: Long
		get() = restoreMillis(id)

	val date: OffsetDateTime
		get() = parseDate(id, offset)

	val year: Int
		get() = date.withOffsetSameInstant(ZoneOffset.UTC).year

	val fileName: String
		get() = "$id.$ext"

	val isVideo: Boolean
		get() = isVideo(fileName)
}
