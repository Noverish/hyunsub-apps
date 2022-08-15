package kim.hyunsub.auth.aop

import kim.hyunsub.common.log.Log
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class CorsAspect {
	companion object : Log

	@Around("@annotation(kim.hyunsub.auth.annotation.HyunsubCors)")
	fun cors(joinPoint: ProceedingJoinPoint): Any? {
		val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
		val request = requestAttributes.request
		val response = requestAttributes.response

		val origin = request.getHeader(HttpHeaders.ORIGIN)
			?: return joinPoint.proceed()
		log.info("[CORS AOP] origin: {}", origin)

		if (origin.endsWith(".hyunsub.kim")) {
			response?.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin)
		}
		return joinPoint.proceed()
	}
}
