package kim.hyunsub.auth.model

data class RegisterApiParams(
	val username: String,
	val password: String,
	val captcha: String,
)
