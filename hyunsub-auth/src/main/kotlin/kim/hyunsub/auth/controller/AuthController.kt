package kim.hyunsub.auth.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import kim.hyunsub.auth.config.AppConstants
import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.ErrorCode
import kim.hyunsub.auth.model.JwtPayload
import kim.hyunsub.auth.service.JwtService
import kim.hyunsub.util.log.Log
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.WebUtils
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val jwtService: JwtService) {
	companion object : Log
	private val mapper = jacksonObjectMapper()

	@GetMapping("")
	fun auth(
		request: HttpServletRequest,
		response: HttpServletResponse,
		@RequestHeader("X-Original-URL") originalUrl: String,
		@RequestHeader("X-Original-IP") originalIp: String,
	) {
		val decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8.toString()) // 로깅용

		try {
			val payload = parseJwt(request)
			log.info("[Auth Success] payload={}, ip={}, url={}", payload, originalIp, decodedUrl)
			response.status = HttpStatus.OK.value()
			response.setHeader(AppConstants.AUTH_HEADER_NAME, mapper.writeValueAsString(payload))
		} catch (e: ErrorCodeException) {
			log.info("[Auth Failed] {}: ip={}, url={}", e.message, originalIp, decodedUrl)
			val redirectUrl = "https://${AppConstants.AUTH_DOMAIN}/login?url=$originalUrl"
			response.setHeader("X-Redirect-URL", redirectUrl)
			response.status = HttpStatus.UNAUTHORIZED.value()
		}
	}

	@GetMapping("/file")
	fun authFile(
		request: HttpServletRequest,
		response: HttpServletResponse,
		@RequestHeader("X-Original-URL") originalUrl: String,
		@RequestHeader("X-Original-IP") originalIp: String,
	) {
		val decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8.toString()) // 로깅용

		val payload = try {
			 parseJwt(request)
		} catch (e: ErrorCodeException) {
			log.info("[AuthFile Failed] {}: ip={}, url={}", e.message, originalIp, decodedUrl)
			response.status = HttpStatus.UNAUTHORIZED.value()
			return
		}

		val path = URL(decodedUrl).path
		val allowed = payload.paths.any { path.startsWith(it) }
		if (allowed) {
			log.info("[AuthFile Success] payload={}, ip={}, url={}", payload, originalIp, decodedUrl)
			response.status = HttpStatus.OK.value()
			response.setHeader(AppConstants.AUTH_HEADER_NAME, mapper.writeValueAsString(payload))
		} else {
			log.info("[AuthFile Failed] Forbidden: payload={}, ip={}, url={}", payload, originalIp, decodedUrl)
			response.status = HttpStatus.FORBIDDEN.value()
		}
	}

	private fun parseJwt(request: HttpServletRequest): JwtPayload {
		val cookie = WebUtils.getCookie(request, AppConstants.JWT_COOKIE_NAME)
			?: throw ErrorCodeException(ErrorCode.NOT_LOGIN)

		try {
			return jwtService.verify(cookie.value)
		} catch (e: SignatureException) {
			throw ErrorCodeException(ErrorCode.INVALID_JWT)
		} catch (e: ExpiredJwtException) {
			throw ErrorCodeException(ErrorCode.EXPIRED_JWT)
		}
	}
}
