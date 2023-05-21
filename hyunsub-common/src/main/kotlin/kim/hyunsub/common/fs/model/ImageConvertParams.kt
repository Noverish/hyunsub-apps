package kim.hyunsub.common.fs.model

data class ImageConvertParams(
	val input: String,
	val output: String,
	val resize: String?,
	val quality: Int?,
	val gravity: String? = null,
	val extent: String? = null,
)
