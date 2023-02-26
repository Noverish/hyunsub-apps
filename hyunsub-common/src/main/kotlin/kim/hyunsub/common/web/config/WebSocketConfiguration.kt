package kim.hyunsub.common.web.config

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kim.hyunsub.common.web.aop.AuthorityCheckAspect
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfiguration : WebSocketMessageBrokerConfigurer {
	override fun registerStompEndpoints(registry: StompEndpointRegistry) {
		registry.addEndpoint("/socket")
			.addInterceptors(WebSocketHandshakeInterceptor())
			.setAllowedOriginPatterns("*")
	}

	class WebSocketHandshakeInterceptor : HandshakeInterceptor {
		private val log = KotlinLogging.logger { }
		private val mapper = jacksonObjectMapper()

		override fun beforeHandshake(req: ServerHttpRequest, res: ServerHttpResponse, handler: WebSocketHandler, attributes: MutableMap<String, Any>): Boolean {
			val userAuthHeader: String? = req.headers[WebConstants.USER_AUTH_HEADER]?.firstOrNull()
			if (userAuthHeader == null) {
				log.warn("[Check Authority] No User Auth")
				throw ErrorCodeException(ErrorCode.NO_USER_AUTH)
			}

			val userAuth: UserAuth? = try {
				mapper.readValue(userAuthHeader)
			} catch (ex: JsonParseException) {
				null
			}
			if (userAuth == null) {
				log.warn("[Check Authority] Invalid User Auth")
				throw ErrorCodeException(ErrorCode.INVALID_USER_AUTH)
			}

			attributes[WebConstants.USER_AUTH_HEADER] = userAuth

			return true
		}

		override fun afterHandshake(req: ServerHttpRequest, res: ServerHttpResponse, handler: WebSocketHandler, exception: Exception?) {

		}
	}
}

val SimpMessageHeaderAccessor.userAuth: UserAuth
	get() = this.sessionAttributes!![WebConstants.USER_AUTH_HEADER] as UserAuth
