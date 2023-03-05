package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.util.decodeBase64
import kim.hyunsub.common.util.toBase64
import kim.hyunsub.common.util.toByteArray
import kim.hyunsub.common.util.toLong
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "photo2")
data class PhotoV2(
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
	val original: String,

	@Column(nullable = false)
	val ext: String,
) {
	companion object {
		fun generateId(millis: Long, hash: String, i: Int = 0): String {
			val base64 = millis.toByteArray().toBase64()
			val subBase64 = base64.substring(3, 11)
			val subHash = hash.substring(i, i + 8)
			return "$subBase64$subHash"
		}

		fun restoreMillis(id: String): Long {
			val subBase64 = id.substring(0, 8)
			val base64 = "AAA$subBase64="
			return base64.decodeBase64().toLong()
		}
	}
}
