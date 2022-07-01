package kim.hyunsub.util.kms

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("kms")
data class KmsProperties(
	val profile: String,
	val keyId: String,
	val properties: Map<String, String> = mutableMapOf(),
)
