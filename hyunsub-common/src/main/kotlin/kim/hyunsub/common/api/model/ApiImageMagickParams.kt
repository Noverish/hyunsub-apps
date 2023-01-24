package kim.hyunsub.common.api.model

data class ApiImageMagickParams(
	val input: String,
	val output: String,
	val options: List<String>,
)
