package kim.hyunsub.common.api.model

data class EncodeParams(
	val input: String,
	val options: String,
	val output: String,
	val callback: String?,
)
