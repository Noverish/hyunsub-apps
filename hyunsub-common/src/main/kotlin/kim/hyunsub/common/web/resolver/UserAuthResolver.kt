package kim.hyunsub.common.web.resolver

import jakarta.servlet.http.HttpServletRequest
import kim.hyunsub.common.web.interceptor.UserAuthInterceptor
import kim.hyunsub.common.web.model.UserAuth
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserAuthResolver : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean =
		parameter.parameterType == UserAuth::class.java

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?,
	): Any? {
		return webRequest.getNativeRequest(HttpServletRequest::class.java)
			?.getAttribute(UserAuthInterceptor.USER_AUTH_ATTR)
	}
}
