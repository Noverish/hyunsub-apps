package kim.hyunsub.dutch.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class DutchWebConfiguration(
	private val dutchMemberAuthInterceptor: DutchMemberAuthInterceptor,
) : WebMvcConfigurer {
	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(DutchMemberAuthResolver())
	}

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(dutchMemberAuthInterceptor)
			.addPathPatterns("/**")
			.order(10)
	}
}
