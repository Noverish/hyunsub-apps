package kim.hyunsub.common.api.model

data class ApiEncodeParams(
	val input: String,
	val options: String,
	val output: String,
	val callback: String?,
)
