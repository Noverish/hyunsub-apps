package kim.hyunsub.common.kms

import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.StringUtils
import javax.sql.DataSource

@Configuration
@ConditionalOnProperty(prefix = "api", name = ["profile", "key-id"])
@EnableConfigurationProperties(KmsProperties::class)
class KmsConfiguration {
	@Bean
	fun dependsOnPostProcessor(): BeanFactoryPostProcessor {
		return BeanFactoryPostProcessor { beanFactory ->
			BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, DataSource::class.java, true, false)
				.map { beanFactory.getBeanDefinition(it) }
				.forEach { it.setDependsOn(*StringUtils.addStringToArray(it.dependsOn, "kmsInitializer")) }
		}
	}
}
