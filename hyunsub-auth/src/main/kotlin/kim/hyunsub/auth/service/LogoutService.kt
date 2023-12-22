package kim.hyunsub.auth.service

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service

@Service
class LogoutService(
	private val cookieGenerator: CookieGenerator,
) {
	fun logout(response: HttpServletResponse? = null) {
		val cookie = cookieGenerator.generateLogoutCookie()
		response?.addCookie(cookie)
	}
}
