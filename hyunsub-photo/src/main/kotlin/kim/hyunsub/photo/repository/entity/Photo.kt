package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.util.toByteArray
import kim.hyunsub.common.util.toHex
import kim.hyunsub.photo.model.PhotoDateType

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
	val idNew: String,
	val pairPhotoIdNew: String? = null,
) {
	companion object {
		fun generateId(millis: Long, hash: String, i: Int = 0): String {
			val hex = millis.toByteArray().toHex()
			val subHex = hex.substring(5)
			val subHash = hash.substring(i, i + 5)
			return "$subHex$subHash"
		}
	}

	val fileName: String
		get() = "$id.$ext"
}
