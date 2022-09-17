package kim.hyunsub.common.api

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("api")
data class ApiProperties(
	val host: String,
	val token: String,
	val nonceBase: String = "/hyunsub/file/upload"
)
