package kim.hyunsub.auth.service

import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.config.TokenProperties
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie

@Service
class CookieGenerator(
	private val tokenProperties: TokenProperties,
) {
	fun generateLoginCookie(token: String, remember: Boolean): Cookie =
		Cookie(WebConstants.TOKEN_COOKIE_NAME, token).apply {
			domain = AuthConstants.TOKEN_COOKIE_DOMAIN
			path = "/"
			if (remember) {
				maxAge = tokenProperties.duration.seconds.toInt()
			}
		}

	fun generateLogoutCookie(): Cookie =
		Cookie(WebConstants.TOKEN_COOKIE_NAME, "").apply {
			domain = AuthConstants.TOKEN_COOKIE_DOMAIN
			maxAge = 0
			path = "/"
		}
}
