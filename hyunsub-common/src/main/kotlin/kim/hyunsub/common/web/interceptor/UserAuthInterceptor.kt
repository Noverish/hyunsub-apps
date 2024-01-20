package kim.hyunsub.common.web.interceptor

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.common.util.getAnnotation
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.annotation.IgnoreAuthorize
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

class UserAuthInterceptor(
	private val appProperties: AppProperties,
) : HandlerInterceptor {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	companion object {
		const val USER_AUTH_ATTR = "kim.hyunsub.UserAuth"
	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		if (!appProperties.useAuth) {
			return true
		}

		val method = (handler as? HandlerMethod)?.method ?: return false

		val ignoreAuthorize = method.getAnnotation<IgnoreAuthorize>()
		if (ignoreAuthorize != null) {
			return true
		}

		val path = request.requestURI
		val header = request.getHeader(WebConstants.USER_AUTH_HEADER)
		if (header == null) {
			log.warn { "[Authority] $path - No User Auth Header" }
			throw ErrorCodeException(ErrorCode.NO_USER_AUTH)
		}

		val userAuth: UserAuth = try {
			mapper.readValue(header)
		} catch (ex: JsonParseException) {
			log.warn(ex) { "[Authority] $path - JsonParseException: header=$header" }
			throw ErrorCodeException(ErrorCode.INVALID_USER_AUTH)
		}

		try {
			// Check method authority
			method.getAnnotation<Authorized>()?.let {
				checkAuthorityWithAnnotation(userAuth, it)
			}

			// Check class authority
			method.declaringClass.getAnnotation<Authorized>()?.let {
				checkAuthorityWithAnnotation(userAuth, it)
			}

			// Check service authority
			appProperties.authorities?.let {
				checkAuthorityWithAnnotation(userAuth, Authorized(it.toTypedArray()))
			}
		} catch (ex: ErrorCodeException) {
			log.warn { "[Authority] $path - No Authority: authorities=${userAuth.names}" }
		}

		request.setAttribute(USER_AUTH_ATTR, userAuth)

		return true
	}

	private fun checkAuthorityWithAnnotation(userAuth: UserAuth, annotation: Authorized) {
		val hasAllAuthorities = userAuth.names.containsAll(annotation.authorities.toList())
		if (!hasAllAuthorities) {
			throw ErrorCodeException(ErrorCode.NO_AUTHORITY)
		}
	}
}
