package kim.hyunsub.common.kms

import mu.KotlinLogging
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.EnumerablePropertySource
import org.springframework.core.env.Environment

class KmsInitializer(
	environment: Environment,
) {
	private val log = KotlinLogging.logger { }

	init {
		init(environment as ConfigurableEnvironment)
	}

	private fun init(env: ConfigurableEnvironment) {
		val profile = env.getProperty("kms.profile") ?: return
		val keyId = env.getProperty("kms.key-id") ?: return
		val properties = env.propertySources
			.mapNotNull { it as? EnumerablePropertySource }
			.flatMap { it.propertyNames.toList() }
			.filter { it.startsWith("kms.properties.") }
			.mapNotNull { env.getProperty(it) }
			.flatMap { it.split(",") }
			.map { it.trim() }

		for (property in properties) {
			val encrypted = env.getProperty(property) ?: continue
			log.debug("KMS Properties: {} - {}", property, encrypted)
			val decrypted = KmsEncryptor.decrypt(profile, keyId, encrypted)
			log.debug("KMS Properties: {} - {}", property, decrypted)
			System.setProperty(property, decrypted)
		}
	}
}
