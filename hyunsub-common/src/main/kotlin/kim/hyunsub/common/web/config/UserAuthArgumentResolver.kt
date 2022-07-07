package kim.hyunsub.common.web.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

class UserAuthArgumentResolver : HandlerMethodArgumentResolver {
	private val mapper = jacksonObjectMapper()

	override fun supportsParameter(parameter: MethodParameter): Boolean =
		parameter.parameterType == UserAuth::class.java

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?
	): Any {
		val request = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
		val header = request.getHeader(WebConstants.USER_AUTH_HEADER)
			?: throw ErrorCodeException(ErrorCode.NOT_LOGIN)
		return mapper.readValue<UserAuth>(header)
	}
}
