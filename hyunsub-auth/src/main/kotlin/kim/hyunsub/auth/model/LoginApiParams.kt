package kim.hyunsub.auth.model

data class LoginApiParams(
	val username: String,
	val password: String,
	val remember: Boolean,
	val captcha: String?,
)
