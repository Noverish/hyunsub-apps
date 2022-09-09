package kim.hyunsub.auth.model

data class RegisterParams(
	val username: String,
	val password: String,
	val captcha: String,
	val remoteAddr: String,
)
