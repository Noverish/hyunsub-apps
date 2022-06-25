package kim.hyunsub.auth.model.captcha

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * https://developers.google.com/recaptcha/docs/verify
 */
data class CaptchaVerifyResult(
	val success: Boolean,
	@field:JsonProperty("challenge_ts")
	val challengeTs: String?,
	val hostname: String?,
	@field:JsonProperty("error-codes")
	val errorCodes: List<String>?,
)
