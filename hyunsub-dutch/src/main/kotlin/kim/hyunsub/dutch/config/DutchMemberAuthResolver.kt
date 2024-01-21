package kim.hyunsub.dutch.config

import jakarta.servlet.http.HttpServletRequest
import kim.hyunsub.dutch.model.DutchMemberAuth
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class DutchMemberAuthResolver : HandlerMethodArgumentResolver {
	override fun supportsParameter(parameter: MethodParameter): Boolean =
		parameter.parameterType == DutchMemberAuth::class.java

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?,
	): Any? {
		return webRequest.getNativeRequest(HttpServletRequest::class.java)
			?.getAttribute(DutchMemberAuthInterceptor.DUTCH_MEMBER_AUTH_ATTR)
	}
}
