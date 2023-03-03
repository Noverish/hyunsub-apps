package kim.hyunsub.common.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CommonWebMvcConfiguration : WebMvcConfigurer {
	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(UserAuthArgumentResolver())
	}

	/**
	 * Make always 'Content-Type' response header as 'application/json'
	 */
	override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
		configurer.ignoreAcceptHeader(true)
		configurer.defaultContentType(MediaType.APPLICATION_JSON)
	}
}
