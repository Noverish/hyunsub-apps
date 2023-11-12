package kim.hyunsub.common.web.config

import kim.hyunsub.common.jackson.localDateFormatter
import kim.hyunsub.common.web.interceptor.WebLoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.MediaType
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDate

@Configuration
class CommonWebConfiguration : WebMvcConfigurer {
	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(UserAuthArgumentResolver())
	}

	override fun addFormatters(registry: FormatterRegistry) {
		registry.addConverter(String::class.java, LocalDate::class.java) {
			LocalDate.parse(it, localDateFormatter)
		}
	}

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(WebLoggingInterceptor())
			.addPathPatterns("/**")
			.order(0)
	}

	/**
	 * Make always 'Content-Type' response header as 'application/json'
	 */
	override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
		configurer.ignoreAcceptHeader(true)
		configurer.defaultContentType(MediaType.APPLICATION_JSON)
	}
}