package kim.hyunsub.auth.service

import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.config.JwtProperties
import kim.hyunsub.common.web.config.WebConstants
import org.springframework.stereotype.Service
import javax.servlet.http.Cookie

@Service
class CookieGenerator(
	private val jwtProperties: JwtProperties,
) {
	fun generateLoginCookie(token: String): Cookie =
		Cookie(WebConstants.TOKEN_COOKIE_NAME, token).apply {
			domain = AuthConstants.TOKEN_COOKIE_DOMAIN
			maxAge = jwtProperties.duration.seconds.toInt()
			path = "/"
		}

	fun generateLogoutCookie(): Cookie =
		Cookie(WebConstants.TOKEN_COOKIE_NAME, "").apply {
			domain = AuthConstants.TOKEN_COOKIE_DOMAIN
			maxAge = 0
			path = "/"
		}
}
