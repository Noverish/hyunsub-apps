package kim.hyunsub.auth.controller

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.service.TokenService
import kim.hyunsub.auth.service.UserAuthService
import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.common.web.annotation.IgnoreAuthorize
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.WebUtils
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@IgnoreAuthorize
@RestController
@RequestMapping("/api/v1/nginx/auth")
class NginxAuthController(
	private val tokenService: TokenService,
	private val appProperties: AppProperties,
	private val userAuthService: UserAuthService,
	private val userRepository: UserRepository,
) {
	private val log = KotlinLogging.logger { }

	private val mapper = jacksonObjectMapper()

	@GetMapping("")
	fun auth(
		request: HttpServletRequest,
		response: HttpServletResponse,
		@RequestHeader("X-Original-URL") originalUrl: String,
		@RequestHeader("X-Original-IP") originalIp: String,
		@RequestHeader("X-Original-Method", required = false) originalMethod: String?,
	) {
		if (HttpMethod.OPTIONS.name().equals(originalMethod, ignoreCase = true)) {
			response.status = HttpStatus.OK.value()
			response.setHeader(WebConstants.USER_AUTH_HEADER, "")
			return
		}

		val decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8.toString())
		val isFromApi = URL(decodedUrl).path.startsWith("/api")

		try {
			val isValidUrl = URL(decodedUrl).host.endsWith(".hyunsub.kim")
			if (!isValidUrl) {
				throw ErrorCodeException(ErrorCode.INVALID_URL, mapOf("url" to decodedUrl))
			}

			val userAuth = parseToken(request)

			log.info { "[Auth Success] userAuth=$userAuth, ip=$originalIp, url=$decodedUrl, method=$originalMethod" }
			response.status = HttpStatus.OK.value()
			response.setHeader(WebConstants.USER_AUTH_HEADER, mapper.writeValueAsString(userAuth))
		} catch (e: ErrorCodeException) {
			log.info { "[Auth Failed] ${e.message}: ip=$originalIp, url=$decodedUrl, method=$originalMethod" }
			response.status = HttpStatus.UNAUTHORIZED.value()

			if (isFromApi) {
				val res = mapOf("code" to e.errorCode.code, "msg" to e.errorCode.msg, "payload" to e.payload)
				response.setHeader("X-Auth-Failed", mapper.writeValueAsString(res))
			} else {
				val encodedUrl = URLEncoder.encode(decodedUrl, StandardCharsets.UTF_8.toString())
				val redirectUrl = "https://${appProperties.host}/login?url=$encodedUrl"
				response.setHeader("X-Redirect-URL", redirectUrl)
			}
		} catch (e: JacksonException) {
			log.info { "[Auth Failed] ${e.message}: ip=$originalIp, url=$decodedUrl, method=$originalMethod" }
			response.status = HttpStatus.UNAUTHORIZED.value()
			response.setHeader("X-Auth-Failed", mapper.writeValueAsString(mapOf("msg" to e.message)))
		}
	}

	@GetMapping("/file")
	fun authFile(
		request: HttpServletRequest,
		response: HttpServletResponse,
		@RequestHeader("X-Original-URL") originalUrl: String,
		@RequestHeader("X-Original-IP") originalIp: String,
		@RequestHeader("X-Original-Method", required = false) originalMethod: String?,
		@RequestHeader("X-Original-Referer", required = false) originalReferer: String?,
	) {
		if (HttpMethod.OPTIONS.name().equals(originalMethod, ignoreCase = true)) {
			response.status = HttpStatus.OK.value()
			response.setHeader(WebConstants.USER_AUTH_HEADER, "")
			return
		}

		val decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8.toString())
		val path = URL(decodedUrl).path
		val isFromApi = path.startsWith("/api") || path.startsWith("/upload")

		try {
			val isValidUrl = URL(decodedUrl).host.endsWith("file.hyunsub.kim")
			if (!isValidUrl) {
				throw ErrorCodeException(ErrorCode.INVALID_URL, mapOf("url" to decodedUrl))
			}

			val userAuth = parseToken(request)

			if (!originalReferer.isNullOrEmpty() && originalReferer.endsWith("drive.hyunsub.kim/")) {
				log.info { "[AuthFile Success] ip=$originalIp, url=$decodedUrl, method=$originalMethod, referer=$originalReferer, userAuth=$userAuth" }
				response.status = HttpStatus.OK.value()
				response.setHeader(WebConstants.USER_AUTH_HEADER, mapper.writeValueAsString(userAuth))
				return
			}

			val paths = if (isFromApi) userAuth.apis else userAuth.paths
			val filledPath = path.replace("{idNo}", userAuth.idNo)
			val allowed = paths.any { filledPath.startsWith(it) }
			if (!allowed) {
				throw ErrorCodeException(ErrorCode.NO_AUTHORITY, mapOf("path" to path))
			}

			log.info { "[AuthFile Success] ip=$originalIp, url=$decodedUrl, method=$originalMethod, referer=$originalReferer, userAuth=$userAuth" }
			response.status = HttpStatus.OK.value()
			response.setHeader(WebConstants.USER_AUTH_HEADER, mapper.writeValueAsString(userAuth))
		} catch (e: ErrorCodeException) {
			log.info { "[AuthFile Failed] ${e.message}: ip=$originalIp, url=$decodedUrl, method=$originalMethod, referer=$originalReferer" }
			response.status = HttpStatus.UNAUTHORIZED.value()

			if (isFromApi) {
				val res = mapOf("code" to e.errorCode.code, "msg" to e.errorCode.msg, "payload" to e.payload)
				response.setHeader("X-Auth-Failed", mapper.writeValueAsString(res))
			} else {
				val res =
					if (e.errorCode == ErrorCode.NO_AUTHORITY) HttpStatus.FORBIDDEN.toString() else HttpStatus.UNAUTHORIZED.toString()
				response.setHeader("X-Auth-Failed", res)
			}
		}
	}

	private fun parseToken(request: HttpServletRequest): UserAuth {
		val cookie = WebUtils.getCookie(request, WebConstants.TOKEN_COOKIE_NAME)
			?: throw ErrorCodeException(ErrorCode.NOT_LOGIN)

		try {
			val tokenPayload = tokenService.verify(cookie.value)

			userRepository.findByIdOrNull(tokenPayload.idNo)
				?: throw ErrorCodeException(ErrorCode.NO_SUCH_USER)

			return userAuthService.getUserAuth(tokenPayload.idNo)
		} catch (e: SignatureException) {
			throw ErrorCodeException(ErrorCode.INVALID_JWT)
		} catch (e: ExpiredJwtException) {
			throw ErrorCodeException(ErrorCode.EXPIRED_JWT)
		}
	}
}
