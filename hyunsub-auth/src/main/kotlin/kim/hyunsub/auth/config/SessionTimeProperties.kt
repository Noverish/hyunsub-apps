package kim.hyunsub.auth.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties("session")
data class SessionTimeProperties(
	val sessionTimeMap: Map<String, SessionTimeInfo>
) {
	fun getSessionTime(type: Class<*>): Duration =
		sessionTimeMap.values.firstOrNull { it.type == type }?.time ?: Duration.ofHours(1)
}

data class SessionTimeInfo(
	val type: Class<*>,
	val time: Duration,
)
