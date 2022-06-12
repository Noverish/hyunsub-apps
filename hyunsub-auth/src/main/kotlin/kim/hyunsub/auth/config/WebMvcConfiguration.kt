package kim.hyunsub.auth.config

import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.service.JwtService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
	private val jwtService: JwtService,
	private val userRepository: UserRepository,
): WebMvcConfigurer {
	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(UserArgumentResolver(jwtService, userRepository))
	}
}
