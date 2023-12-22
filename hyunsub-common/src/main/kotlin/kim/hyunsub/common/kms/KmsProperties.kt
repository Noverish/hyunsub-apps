package kim.hyunsub.common.kms

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("kms")
data class KmsProperties(
	val profile: String,
	val keyId: String,
)
