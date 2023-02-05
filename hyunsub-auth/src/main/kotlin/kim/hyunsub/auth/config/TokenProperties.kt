package kim.hyunsub.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties("token")
data class TokenProperties(
	val private: String,
	val public: String,
	val duration: Duration,
)
