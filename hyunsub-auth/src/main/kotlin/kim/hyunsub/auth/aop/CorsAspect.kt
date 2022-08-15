package kim.hyunsub.auth.aop

import kim.hyunsub.common.log.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URL

@Aspect
@Component
class CorsAspect {
	companion object : Log

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	fun isRestControllerClass() = Unit

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	fun isGetMappingMethod() = Unit

	@Around("isRestControllerClass() && isGetMappingMethod()")
	fun cors(joinPoint: ProceedingJoinPoint): Any? {
		val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
		val request = requestAttributes.request
		val response = requestAttributes.response

		for (headerName in request.headerNames) {
			val headerValue = request.getHeader(headerName)
			log.info("[CORS AOP] {}={}", headerName, headerValue)
		}

		val urlHeader = request.getHeader("X-Original-URL")
			?: return joinPoint.proceed()
		log.info("[CORS AOP] url: {}", urlHeader)

		val host = URL(urlHeader).host
		if (host.endsWith(".hyunsub.kim")) {
			response?.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://$host")
		}

		return joinPoint.proceed()
	}
}
