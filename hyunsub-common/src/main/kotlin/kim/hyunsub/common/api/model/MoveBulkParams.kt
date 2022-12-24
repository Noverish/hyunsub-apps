package kim.hyunsub.common.api.model

data class MoveBulkParams(
	val from: String,
	val to: String,
	val files: List<String>,
)
