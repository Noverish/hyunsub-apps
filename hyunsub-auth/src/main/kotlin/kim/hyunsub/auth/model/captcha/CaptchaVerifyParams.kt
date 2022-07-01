package kim.hyunsub.auth.model.captcha

/**
 * https://developers.google.com/recaptcha/docs/verify
 */
data class CaptchaVerifyParams(
	val secret: String,
	val response: String,
	val remoteip: String?,
)
