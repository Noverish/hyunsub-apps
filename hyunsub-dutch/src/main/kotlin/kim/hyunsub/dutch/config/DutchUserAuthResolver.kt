package kim.hyunsub.dutch.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.service.DutchUserAuthService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Service
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.HandlerMapping

@Service
class DutchUserAuthResolver(
	val dutchUserAuthService: DutchUserAuthService,
) : HandlerMethodArgumentResolver {
	private val mapper = jacksonObjectMapper()

	override fun supportsParameter(parameter: MethodParameter): Boolean =
		parameter.parameterType == DutchMemberAuth::class.java

	override fun resolveArgument(
		parameter: MethodParameter,
		mavContainer: ModelAndViewContainer?,
		webRequest: NativeWebRequest,
		binderFactory: WebDataBinderFactory?,
	): Any? {
		val req = webRequest.getNativeRequest(HttpServletRequest::class.java) ?: return null

		val pathVariableAttr = req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
		val pathVariableNode = mapper.convertValue<JsonNode>(pathVariableAttr)
		val tripId = pathVariableNode["tripId"]?.asText() ?: return null

		return dutchUserAuthService.parse(req, tripId)
	}
}
