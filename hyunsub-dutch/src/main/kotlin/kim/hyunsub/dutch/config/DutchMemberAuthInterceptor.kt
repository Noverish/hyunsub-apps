package kim.hyunsub.dutch.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.common.util.getAnnotation
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.service.DutchUserAuthService
import org.springframework.messaging.handler.HandlerMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.HandlerMapping

@Component
class DutchMemberAuthInterceptor(
	private val dutchUserAuthService: DutchUserAuthService,
) : HandlerInterceptor {
	private val mapper = jacksonObjectMapper()

	companion object {
		const val DUTCH_MEMBER_AUTH_ATTR = "kim.hyunsub.dutch.DutchMemberAuth"
	}

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		val pathVariableAttr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)
		val pathVariableNode = mapper.convertValue<JsonNode>(pathVariableAttr)
		val tripId = pathVariableNode["tripId"]?.asText() ?: return true

		val memberAuth = dutchUserAuthService.parse(request, tripId)
		if (memberAuth != null) {
			request.setAttribute(DUTCH_MEMBER_AUTH_ATTR, memberAuth)
			return true
		}

		val method = (handler as? HandlerMethod)?.method ?: return true
		method.getAnnotation<DutchIgnoreAuthorize>()
			?: throw ErrorCodeException(ErrorCode.NO_USER_AUTH)

		return true
	}
}
