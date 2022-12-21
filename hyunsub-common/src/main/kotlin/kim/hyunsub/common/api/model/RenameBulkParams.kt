package kim.hyunsub.common.api.model

data class RenameBulkParams(
	val path: String,
	val renames: List<RenameBulkParamData>,
)

data class RenameBulkParamData(
	val from: String,
	val to: String,
)
