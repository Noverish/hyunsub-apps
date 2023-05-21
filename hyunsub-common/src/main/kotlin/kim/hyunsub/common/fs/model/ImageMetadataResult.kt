package kim.hyunsub.common.fs.model

data class ImageMetadataResult(
	val format: String,
	val width: Int,
	val height: Int,
	val quality: Int?,
	val filesize: String?,
)
