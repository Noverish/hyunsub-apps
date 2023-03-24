package kim.hyunsub.photo.model.dto

data class PhotoUploadParams(
	val nonce: String,
	val name: String,
	val albumId: String?,
)
