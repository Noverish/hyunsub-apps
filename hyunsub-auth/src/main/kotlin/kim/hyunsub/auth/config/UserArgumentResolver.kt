package kim.hyunsub.auth.config

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import kim.hyunsub.auth.exception.ErrorCodeException
import kim.hyunsub.auth.model.ErrorCode
import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.repository.entity.User
import kim.hyunsub.auth.service.JwtService
import org.springframework.core.MethodParameter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class UserArgumentResolver(
	private val jwtService: JwtService,
	private val userRepository: UserRepository,
) : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean =
		parameter.parameterType == User::class.java

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): User {
		val req = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
		val jwt = req.cookies.firstOrNull { it.name == AppConstants.JWT_COOKIE_NAME }?.value
			?: throw ErrorCodeException(ErrorCode.NOT_LOGIN)

		val payload = try {
			jwtService.verify(jwt)
		} catch (e: SignatureException) {
			throw ErrorCodeException(ErrorCode.INVALID_JWT)
		} catch (e: ExpiredJwtException) {
			throw ErrorCodeException(ErrorCode.EXPIRED_JWT)
		}

		return userRepository.findByIdOrNull(payload.idNo)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_USER)
	}
}