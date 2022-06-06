package kim.hyunsub.auth.model

data class LoginResult(
	val idNo: String,
	val jwt: String,
)
