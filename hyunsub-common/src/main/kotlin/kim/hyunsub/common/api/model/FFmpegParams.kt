package kim.hyunsub.common.api.model

data class FFmpegParams(
	val input: String,
	val options: String,
	val output: String,
	val data: Any,
)
