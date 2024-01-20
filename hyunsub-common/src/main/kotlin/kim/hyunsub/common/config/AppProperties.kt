package kim.hyunsub.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
data class AppProperties(
	val host: String,
	val authorities: List<String>?,
	val useAuth: Boolean = true,
)
