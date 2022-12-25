package kim.hyunsub.common.api.model

data class ApiRenameBulkParams(
	val path: String,
	val renames: List<ApiRenameBulkParamData>,
)

data class ApiRenameBulkParamData(
	val from: String,
	val to: String,
)
