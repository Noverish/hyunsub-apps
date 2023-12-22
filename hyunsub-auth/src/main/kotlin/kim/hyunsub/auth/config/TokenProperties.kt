package kim.hyunsub.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("token")
data class TokenProperties(
	val private: String,
	val public: String,
	val duration: Duration,
)
