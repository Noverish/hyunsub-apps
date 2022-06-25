package kim.hyunsub.auth.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.auth.model.LoginFailureSession
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableConfigurationProperties(SessionTimeProperties::class)
class SessionConfiguration {
	@Bean
	fun sessionRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, LoginFailureSession> {
		val serializer = Jackson2JsonRedisSerializer(LoginFailureSession::class.java)
		serializer.setObjectMapper(jacksonObjectMapper())

		val redisTemplate: RedisTemplate<String, LoginFailureSession> = RedisTemplate<String, LoginFailureSession>()
		redisTemplate.connectionFactory = redisConnectionFactory
		redisTemplate.keySerializer = StringRedisSerializer.UTF_8
		redisTemplate.valueSerializer = serializer
		return redisTemplate
	}
}
