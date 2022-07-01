package kim.hyunsub.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties("jwt")
data class JwtProperties(
	val key: String,
	val iv: String,
	val duration: Duration,
)
