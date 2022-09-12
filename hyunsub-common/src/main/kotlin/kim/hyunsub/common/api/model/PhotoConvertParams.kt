package kim.hyunsub.common.api.model

data class PhotoConvertParams(
	val input: String,
	val output: String,
	val resize: String?,
	val quality: Int?,
)
