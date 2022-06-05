package kim.hyunsub.util.kms

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("kms.datasource")
data class KmsDataSourceProperties(
	val profile: String,
	val keyId: String,
	val url: String,
	val username: String,
	val password: String,
)
