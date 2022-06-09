package kim.hyunsub.auth.controller

import kim.hyunsub.auth.config.AppConstants
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.service.LoginService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/login")
class LoginApiController(
	private val loginService: LoginService,
) {
	@PostMapping("")
	fun login(
		response: HttpServletResponse,
		@RequestBody params: LoginParams,
	): LoginResult {
		return loginService.login(params).also {
			val cookie = Cookie(AppConstants.JWT_COOKIE_NAME, it.jwt)
			cookie.domain = AppConstants.JWT_COOKIE_DOMAIN
			response.addCookie(cookie)
		}
	}
}
