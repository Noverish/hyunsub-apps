package kim.hyunsub.common.kms

import kim.hyunsub.common.log.Log
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class KmsInitializer(
	environment: Environment,
	kmsProperties: KmsProperties,
) {
	companion object : Log

	init {
		val profile = kmsProperties.profile
		val keyId = kmsProperties.keyId

		val properties = kmsProperties.properties.values.flatMap { it.split(",") }
		for (property in properties) {
			val encrypted = (environment as? ConfigurableEnvironment)?.getProperty(property) ?: continue
			log.debug("KMS Properties: {} - {}", property, encrypted)
			val decrypted = KmsEncryptor.decrypt(profile, keyId, encrypted)
			log.debug("KMS Properties: {} - {}", property, decrypted)
			System.setProperty(property, decrypted)
		}
	}
}
