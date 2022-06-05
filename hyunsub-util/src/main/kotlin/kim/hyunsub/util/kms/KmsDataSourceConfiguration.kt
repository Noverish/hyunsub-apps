package kim.hyunsub.util.kms

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(KmsDataSourceProperties::class)
class KmsDataSourceConfiguration {
	@Bean
	fun dataSource(properties: KmsDataSourceProperties): DataSource {
		val profile = properties.profile
		val keyId = properties.keyId
		val url = KmsEncryptor.decrypt(profile, keyId, properties.url)
		val username = KmsEncryptor.decrypt(profile, keyId, properties.username)
		val password = KmsEncryptor.decrypt(profile, keyId, properties.password)
		return DataSourceBuilder.create()
			.url(url)
			.username(username)
			.password(password)
			.build()
	}
}
