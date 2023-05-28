package kim.hyunsub.common.kms

import mu.KotlinLogging
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Environment

class KmsInitializer(
	environment: Environment,
	kmsProperties: KmsProperties,
) {
	private val log = KotlinLogging.logger { }

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
