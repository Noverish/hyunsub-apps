package kim.hyunsub.auth.model.user

data class ApiUser(
	val idNo: String,
	val username: String,
	val authorities: List<Int>,
)
