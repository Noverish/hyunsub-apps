package kim.hyunsub.common.api

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@ConditionalOnProperty(prefix = "api", name = ["host", "token"])
@EnableConfigurationProperties(ApiProperties::class)
class ApiConfiguration {
	@Bean
	fun apiClient(apiProperties: ApiProperties): ApiClient {
		return ApiClient(RestTemplate(), apiProperties)
	}

	@Bean
	fun apiCaller(apiClient: ApiClient): ApiCaller {
		return ApiCaller(apiClient)
	}
}
