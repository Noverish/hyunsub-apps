package kim.hyunsub.common.api.model

data class ApiImageMetadataResult(
	val format: String,
	val width: Int,
	val height: Int,
	val quality: Int?,
	val filesize: String?,
)
