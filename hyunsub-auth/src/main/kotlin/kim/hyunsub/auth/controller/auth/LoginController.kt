package kim.hyunsub.auth.controller.auth

import kim.hyunsub.auth.model.LoginApiParams
import kim.hyunsub.auth.model.LoginParams
import kim.hyunsub.auth.model.LoginResult
import kim.hyunsub.auth.service.CookieGenerator
import kim.hyunsub.auth.service.LoginService
import kim.hyunsub.auth.service.RsaKeyService
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.IgnoreAuthorize
import kim.hyunsub.common.web.model.HyunsubHeader
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@IgnoreAuthorize
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
		response: HttpServletResponse,
		@RequestHeader(HyunsubHeader.X_ORIGINAL_IP) remoteAddr: String,
		@RequestBody params: LoginApiParams,
	): LoginResult {
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

		val cookie = cookieGenerator.generateLoginCookie(result.token, params.remember)
		response.addCookie(cookie)

		return result
	}
}
