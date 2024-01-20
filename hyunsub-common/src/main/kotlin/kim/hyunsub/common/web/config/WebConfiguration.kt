package kim.hyunsub.common.web.config

import kim.hyunsub.common.config.AppProperties
import kim.hyunsub.common.jackson.localDateFormatter
import kim.hyunsub.common.web.interceptor.CorsInterceptor
import kim.hyunsub.common.web.interceptor.UserAuthInterceptor
import kim.hyunsub.common.web.interceptor.WebLoggingInterceptor
import kim.hyunsub.common.web.resolver.UserAuthResolver
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.MediaType
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDate

@Configuration
class WebConfiguration(
	private val appProperties: AppProperties,
) : WebMvcConfigurer {
	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(UserAuthResolver())
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

		registry.addInterceptor(UserAuthInterceptor(appProperties))
			.addPathPatterns("/**")
			.order(1)

		registry.addInterceptor(CorsInterceptor())
			.addPathPatterns("/**")
			.order(2)
	}

	/**
	 * Make always 'Content-Type' response header as 'application/json'
	 */
	override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
		configurer.ignoreAcceptHeader(true)
		configurer.defaultContentType(MediaType.APPLICATION_JSON)
	}
}
