package kim.hyunsub.photo.model.api

data class ApiPhotoUploadParams(
	val nonce: String,
	val name: String,
	val millis: Long,
	val albumId: String?,
)
