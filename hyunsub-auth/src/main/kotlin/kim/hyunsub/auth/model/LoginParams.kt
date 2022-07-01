package kim.hyunsub.auth.model

data class LoginParams(
	val username: String,
	val password: String,
	val remember: Boolean,
	val captcha: String?,
	val remoteAddr: String,
)
