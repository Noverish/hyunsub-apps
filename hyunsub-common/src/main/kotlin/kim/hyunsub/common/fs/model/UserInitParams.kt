package kim.hyunsub.common.fs.model

data class UserInitParams(
	val userId: String,
	val dryRun: Boolean = false,
)
