package kim.hyunsub.auth.config

import java.time.Duration

object AppConstants {
	const val BCRYPT_COST = 12
	const val JWT_COOKIE_NAME = "HYUNSUB_JWT"
	const val JWT_COOKIE_DOMAIN = ".hyunsub.kim"
	const val AUTH_HEADER_NAME = "HYUNSUB_AUTH"
	const val AUTH_DOMAIN = "auth2.hyunsub.kim"

	/** 로그인에 몇 번 실패해야 캡차가 뜨는지 */
	const val FAIL_NUM_TO_CAPTCHA = 3
}
