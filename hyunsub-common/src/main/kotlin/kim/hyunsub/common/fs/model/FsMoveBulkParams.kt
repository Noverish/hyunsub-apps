package kim.hyunsub.common.fs.model

data class FsMoveBulkParams(
	val from: String,
	val to: String,
	val files: List<String>,
)
