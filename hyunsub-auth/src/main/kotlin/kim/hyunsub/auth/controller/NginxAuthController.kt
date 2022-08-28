package kim.hyunsub.auth.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import kim.hyunsub.auth.config.AuthConstants
import kim.hyunsub.auth.service.AuthorityService
import kim.hyunsub.auth.service.JwtService
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.WebUtils
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/nginx/auth")
class NginxAuthController(
	private val jwtService: JwtService,
	private val authorityService: AuthorityService
) {
	companion object : Log

	private val mapper = jacksonObjectMapper()

	@GetMapping("")
	fun auth(
		request: HttpServletRequest,
		response: HttpServletResponse,
		@RequestHeader("X-Original-URL") originalUrl: String,
		@RequestHeader("X-Original-IP") originalIp: String,
	) {
		val decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8.toString())

		try {
			val isValidUrl = URL(decodedUrl).host.endsWith(".hyunsub.kim")
			if (!isValidUrl) {
				throw ErrorCodeException(ErrorCode.INVALID_URL, mapOf("url" to decodedUrl))
			}

			val payload = parseJwt(request)
			val userAuth = authorityService.getUserAuth(payload.idNo)
			log.info("[Auth Success] userAuth={}, ip={}, url={}", userAuth, originalIp, decodedUrl)
			response.status = HttpStatus.OK.value()
			response.setHeader(WebConstants.USER_AUTH_HEADER, mapper.writeValueAsString(userAuth))
		} catch (e: ErrorCodeException) {
			log.info("[Auth Failed] {}: ip={}, url={}", e.message, originalIp, decodedUrl)
			response.status = HttpStatus.UNAUTHORIZED.value()

			val isFromApi = URL(decodedUrl).path.startsWith("/api")
			if (isFromApi) {
				val res = mapOf("code" to e.errorCode.code, "msg" to e.errorCode.msg, "payload" to e.payload)
				response.setHeader("X-Auth-Failed", mapper.writeValueAsString(res))
			} else {
				val redirectUrl = "https://${AuthConstants.AUTH_DOMAIN}/login?url=$originalUrl"
				response.setHeader("X-Redirect-URL", redirectUrl)
			}
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
		val allowed = payload.authorityPaths.any { path.startsWith(it) }
		if (allowed) {
			log.info("[AuthFile Success] payload={}, ip={}, url={}", payload, originalIp, decodedUrl)
			response.status = HttpStatus.OK.value()
			response.setHeader(WebConstants.USER_AUTH_HEADER, mapper.writeValueAsString(payload))
		} else {
			log.info("[AuthFile Failed] Forbidden: payload={}, ip={}, url={}", payload, originalIp, decodedUrl)
			response.status = HttpStatus.FORBIDDEN.value()
		}
	}

	private fun parseJwt(request: HttpServletRequest): UserAuth {
		val cookie = WebUtils.getCookie(request, WebConstants.TOKEN_COOKIE_NAME)
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
