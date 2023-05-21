package kim.hyunsub.common.fs.model

data class FsRenameParams(
	val from: String,
	val to: String,
	val override: Boolean = false,
)
