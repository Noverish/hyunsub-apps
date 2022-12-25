package kim.hyunsub.common.api.model

data class ApiMoveBulkParams(
	val from: String,
	val to: String,
	val files: List<String>,
)
