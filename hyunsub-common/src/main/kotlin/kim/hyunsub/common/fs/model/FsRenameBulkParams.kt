package kim.hyunsub.common.fs.model

data class FsRenameBulkParams(
	val path: String,
	val renames: List<FsRenameBulkData>,
)

data class FsRenameBulkData(
	val from: String,
	val to: String,
)
