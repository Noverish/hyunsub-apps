package kim.hyunsub.auth.controller.auth

import kim.hyunsub.auth.model.LoginApiParams
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.model.auth.ValidUrlParams
import kim.hyunsub.auth.model.auth.ValidUrlResult
import kim.hyunsub.auth.service.CookieGenerator
import kim.hyunsub.auth.service.LoginService
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.common.log.Log
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URL
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/auth/login")
class LoginController(
	private val loginService: LoginService,
	private val cookieGenerator: CookieGenerator,
	private val rsaKeyService: RsaKeyService,
) {
	companion object : Log

	@PostMapping("")
	fun login(
		request: HttpServletRequest,
		response: HttpServletResponse,
		@RequestBody params: LoginApiParams,
	): LoginResult {
		val remoteAddr = request.remoteAddr
		log.debug("login: params={}, remoteAddr={}", params, remoteAddr)
		val decryptedParams = LoginParams(
			username = rsaKeyService.decrypt(params.username),
			password = rsaKeyService.decrypt(params.password),
			remember = params.remember,
			captcha = params.captcha,
			remoteAddr = remoteAddr,
		)
		log.debug("login: decryptedParams={}", decryptedParams)

		val result = loginService.login(decryptedParams)

		val cookie = cookieGenerator.generateLoginCookie(result.jwt)
		response.addCookie(cookie)

		return result
	}
}
