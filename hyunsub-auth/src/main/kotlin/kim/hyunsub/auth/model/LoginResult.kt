package kim.hyunsub.auth.model

data class LoginResult(
	val idNo: String,
	val token: String,
	val lang: UserLanguage?,
)
