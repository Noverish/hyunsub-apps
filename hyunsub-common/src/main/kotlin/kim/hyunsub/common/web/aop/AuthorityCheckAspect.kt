package kim.hyunsub.common.web.aop

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * Check authority to access API
 */
@Aspect
@Component
class AuthorityCheckAspect(
	private val appProperties: AppProperties,
) {
	private val log = KotlinLogging.logger { }

	private val mapper = jacksonObjectMapper()

	@Pointcut("within(@kim.hyunsub.common.web.annotation.IgnoreAuthorize *)")
	fun ignoreAuthorize() = Unit

	@Pointcut("execution(* kim.hyunsub.**.controller..*(..))")
	fun controllerMethod() = Unit

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	fun postMapping() = Unit

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	fun getMapping() = Unit

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	fun putMapping() = Unit

	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	fun deleteMapping() = Unit

	@Around("!ignoreAuthorize() && controllerMethod() && (postMapping() || getMapping() || putMapping() || deleteMapping())")
	fun checkAuthority(joinPoint: ProceedingJoinPoint): Any? {
		val requestAttributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
		val request = requestAttributes.request
		val originalIP = request.getHeader("X-Original-IP")
		val originalUrl = request.getHeader("X-Original-URL")
		val method = (joinPoint.signature as MethodSignature).method

		val userAuthHeader: String? = request.getHeader(WebConstants.USER_AUTH_HEADER)
		if (userAuthHeader == null) {
			log.warn("[Check Authority] No User Auth: method={}, originalIP={}, originalUrl={}", method, originalIP, originalUrl)
			throw ErrorCodeException(ErrorCode.NO_USER_AUTH)
		}

		val userAuth: UserAuth? = try {
			mapper.readValue(userAuthHeader)
		} catch (ex: JsonParseException) {
			null
		}
		if (userAuth == null) {
			log.warn("[Check Authority] Invalid User Auth: method={}, userAuthHeader={}, originalIP={}, originalUrl={}", method, userAuthHeader, originalIP, originalUrl)
			throw ErrorCodeException(ErrorCode.INVALID_USER_AUTH)
		}

		try {
			// Check method authority
			method.getAnnotation(Authorized::class.java)?.let {
				checkAuthorityWithAnnotation(userAuth, it)
			}

			// Check class authority
			joinPoint.target.javaClass.getAnnotation(Authorized::class.java)?.let {
				checkAuthorityWithAnnotation(userAuth, it)
			}

			// Check service authority
			appProperties.authorities?.let {
				checkAuthorityWithAnnotation(userAuth, Authorized(it.toTypedArray()))
			}

			log.debug("[Check Authority] Success: method={}, userAuth={}", method, userAuth)
		} catch (e: ErrorCodeException) {
			log.warn("[Check Authority] No Authority: method={}, userAuth={}, originalIP={}, originalUrl={}", method, userAuth, originalIP, originalUrl)
			throw e
		}

		return joinPoint.proceed()
	}

	private fun checkAuthorityWithAnnotation(userAuth: UserAuth, annotation: Authorized) {
		val hasAllAuthorities = userAuth.names.containsAll(annotation.authorities.toList())
		if (!hasAllAuthorities) {
			throw ErrorCodeException(ErrorCode.NO_AUTHORITY)
		}
	}
}
