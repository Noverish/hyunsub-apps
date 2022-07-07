package kim.hyunsub.auth.controller

import kim.hyunsub.auth.model.LogoutResult
import kim.hyunsub.auth.service.CookieGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/logout")
class LogoutController(
	private val cookieGenerator: CookieGenerator,
) {
	@PostMapping("")
	fun logout(response: HttpServletResponse): LogoutResult {
		val cookie = cookieGenerator.generateLogoutCookie()
		response.addCookie(cookie)
		return LogoutResult(true)
	}
}
