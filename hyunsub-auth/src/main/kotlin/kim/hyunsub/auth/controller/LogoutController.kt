package kim.hyunsub.auth.controller

import kim.hyunsub.auth.model.LogoutResult
import kim.hyunsub.auth.service.CookieGenerator
import kim.hyunsub.common.annotation.HyunsubCors
import kim.hyunsub.common.web.annotation.IgnoreAuthorize
import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletResponse

@IgnoreAuthorize
@RestController
@RequestMapping("")
class LogoutController(
	private val cookieGenerator: CookieGenerator,
) {
	private val log = KotlinLogging.logger { }

	@HyunsubCors
	@PostMapping("/api/v1/logout")
	fun logoutApi(
		@RequestHeader(HttpHeaders.REFERER) referer: String,
		response: HttpServletResponse,
	): LogoutResult {
		log.info("[Logout Api] referer={}", referer)

		val cookie = cookieGenerator.generateLogoutCookie()
		response.addCookie(cookie)
		return LogoutResult(true)
	}

	@GetMapping("/logout")
	fun logout(
		@RequestHeader(HttpHeaders.REFERER) referer: String,
		response: HttpServletResponse,
	) {
		log.info("[Logout] referer={}", referer)

		val cookie = cookieGenerator.generateLogoutCookie()
		response.addCookie(cookie)

		val url = URLEncoder.encode(referer, StandardCharsets.UTF_8.toString())
		response.status = HttpStatus.TEMPORARY_REDIRECT.value()
		response.sendRedirect("/login?url=$url")
	}
}
