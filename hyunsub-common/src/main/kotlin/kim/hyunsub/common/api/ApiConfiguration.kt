package kim.hyunsub.common.api

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
@ConditionalOnProperty(prefix = "api", name = ["host", "token"])
@EnableConfigurationProperties(ApiProperties::class)
class ApiConfiguration {
	@Bean
	fun apiClient(apiProperties: ApiProperties, builder: RestTemplateBuilder): ApiClient {
		val restTemplate = builder
			.setConnectTimeout(Duration.ofSeconds(1))
			.setReadTimeout(Duration.ofSeconds(3))
			.build();
		return ApiClient(restTemplate, apiProperties)
	}

	@Bean
	fun apiCaller(apiClient: ApiClient): ApiCaller {
		return ApiCaller(apiClient)
	}

	@Bean
	fun fileUrlConverter(apiProperties: ApiProperties): FileUrlConverter {
		return FileUrlConverter(apiProperties)
	}
}
