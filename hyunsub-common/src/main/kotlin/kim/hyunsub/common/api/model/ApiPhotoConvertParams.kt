package kim.hyunsub.common.api.model

data class ApiPhotoConvertParams(
	val input: String,
	val output: String,
	val resize: String?,
	val quality: Int?,
)
