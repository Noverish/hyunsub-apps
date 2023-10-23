package kim.hyunsub.common.web.aop

import mu.KotlinLogging
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
	private val log = KotlinLogging.logger { }

	@Around("@annotation(kim.hyunsub.common.annotation.HyunsubCors)")
	fun cors(joinPoint: ProceedingJoinPoint): Any? {
		val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
		val request = requestAttributes.request
		val response = requestAttributes.response ?: return joinPoint.proceed()

		val origin = request.getHeader(HttpHeaders.ORIGIN)
			?: return joinPoint.proceed()
		log.info("[CORS AOP] origin: {}", origin)

		if (origin.endsWith(".hyunsub.kim")) {
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin)
			response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
		}
		return joinPoint.proceed()
	}
}
