package kim.hyunsub.common.kms

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsEncodeClient
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.client.FsVideoClient
import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.core.env.Environment
import org.springframework.util.StringUtils
import javax.sql.DataSource

@Configuration
@ConditionalOnProperty(prefix = "kms", name = ["profile", "key-id"])
class KmsConfiguration {
	@Bean
	@ConfigurationProperties("kms")
	fun kmsProperties(): KmsProperties {
		return KmsProperties()
	}

	@Bean
	@DependsOn("kmsProperties")
	fun kmsInitializer(environment: Environment, kmsProperties: KmsProperties): KmsInitializer {
		return KmsInitializer(environment, kmsProperties)
	}

	companion object {
		@Bean
		fun dependsOnPostProcessor(): BeanFactoryPostProcessor {
			return BeanFactoryPostProcessor { beanFactory ->
				beanFactory.kmsDependsOn(DataSource::class.java)
				beanFactory.kmsDependsOn(FsClient::class.java)
				beanFactory.kmsDependsOn(FsVideoClient::class.java)
				beanFactory.kmsDependsOn(FsImageClient::class.java)
				beanFactory.kmsDependsOn(FsEncodeClient::class.java)
			}
		}
	}
}

private fun ConfigurableListableBeanFactory.kmsDependsOn(type: Class<*>) {
	BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this, type, true, false)
		.map { this.getBeanDefinition(it) }
		.forEach { it.setDependsOn(*StringUtils.addStringToArray(it.dependsOn, "kmsInitializer")) }
}
