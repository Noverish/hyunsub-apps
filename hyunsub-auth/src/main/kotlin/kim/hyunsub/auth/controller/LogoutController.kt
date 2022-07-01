package kim.hyunsub.auth.controller

import kim.hyunsub.auth.config.AppConstants
import kim.hyunsub.auth.model.LogoutResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/logout")
class LogoutController {
	@PostMapping("")
	fun logout(response: HttpServletResponse): LogoutResult {
		val cookie = Cookie(AppConstants.JWT_COOKIE_NAME, "").apply {
			domain = AppConstants.JWT_COOKIE_DOMAIN
			maxAge = 0
			path = "/"
		}
		response.addCookie(cookie)
		return LogoutResult(true)
	}
}
