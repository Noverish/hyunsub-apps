package kim.hyunsub.auth.config

import kim.hyunsub.auth.repository.UserRepository
import kim.hyunsub.auth.service.TokenService
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration(
	private val tokenService: TokenService,
	private val userRepository: UserRepository,
) : WebMvcConfigurer {
	override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
		resolvers.add(UserArgumentResolver(tokenService, userRepository))
	}
}
