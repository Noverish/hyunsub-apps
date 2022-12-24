package kim.hyunsub.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class AppProperties(
	val host: String,
	val authority: String?,
)
