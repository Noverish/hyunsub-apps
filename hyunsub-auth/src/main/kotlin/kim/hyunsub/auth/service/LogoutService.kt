package kim.hyunsub.auth.service

import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletResponse

@Service
class LogoutService(
	private val cookieGenerator: CookieGenerator,
) {
	fun logout(response: HttpServletResponse? = null) {
		val cookie = cookieGenerator.generateLogoutCookie()
		response?.addCookie(cookie)
	}
}
