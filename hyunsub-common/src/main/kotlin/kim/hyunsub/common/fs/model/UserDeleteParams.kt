package kim.hyunsub.common.fs.model

data class UserDeleteParams(
	val userId: String,
	val dryRun: Boolean = false,
)
