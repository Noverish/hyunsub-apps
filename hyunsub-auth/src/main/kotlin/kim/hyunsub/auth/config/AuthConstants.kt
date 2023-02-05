package kim.hyunsub.auth.config

object AuthConstants {
	const val BCRYPT_COST = 12
	const val TOKEN_COOKIE_DOMAIN = ".hyunsub.kim"

	/** 로그인에 몇 번 실패해야 캡차가 뜨는지 */
	const val FAIL_NUM_TO_CAPTCHA = 3

	/** idNo 길이 */
	const val ID_NO_LENGTH = 8
}
