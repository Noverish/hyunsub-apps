package kim.hyunsub.auth.model

import com.fasterxml.jackson.annotation.JsonIgnore
import kim.hyunsub.auth.config.AppConstants

data class LoginFailureSession(
	val remoteAddr: String,
	var failCnt: Int = 0,
) {
	@get:JsonIgnore
	val needCaptcha: Boolean
		get() = failCnt >= AppConstants.FAIL_NUM_TO_CAPTCHA
}
