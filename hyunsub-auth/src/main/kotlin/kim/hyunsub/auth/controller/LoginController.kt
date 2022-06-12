package kim.hyunsub.auth.controller

import kim.hyunsub.auth.config.AppConstants
import kim.hyunsub.auth.config.JwtProperties
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.service.LoginService
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.util.log.Log
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/login")
class LoginController(
	private val loginService: LoginService,
	private val jwtProperties: JwtProperties,
	private val rsaKeyService: RsaKeyService,
) {
	companion object : Log

	@PostMapping("")
	fun login(
		response: HttpServletResponse,
		@RequestBody params: LoginParams,
	): LoginResult {
		log.debug("login: params={}", params)
		val decryptedParams = params.copy(
			username = rsaKeyService.decrypt(params.username),
			password = rsaKeyService.decrypt(params.password),
		)
		log.debug("login: decryptedParams={}", decryptedParams)

		return loginService.login(decryptedParams).also {
			val cookie = Cookie(AppConstants.JWT_COOKIE_NAME, it.jwt).apply {
				domain = AppConstants.JWT_COOKIE_DOMAIN
				maxAge = jwtProperties.duration.toSeconds().toInt()
				path = "/"
			}
			response.addCookie(cookie)
		}
	}
}
