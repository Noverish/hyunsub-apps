package kim.hyunsub.common.web.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.common.annotation.HyunsubCors
import kim.hyunsub.common.util.getAnnotation
import org.springframework.http.HttpHeaders
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

class CorsInterceptor : HandlerInterceptor {
	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		(handler as? HandlerMethod)?.method?.getAnnotation<HyunsubCors>() ?: return true

		val origin = request.getHeader(HttpHeaders.ORIGIN) ?: return true
		if (origin.endsWith(".hyunsub.kim")) {
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin)
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
		}

		return true
	}
}
