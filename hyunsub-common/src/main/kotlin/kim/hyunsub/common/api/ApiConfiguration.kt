package kim.hyunsub.common.api

import kim.hyunsub.common.http.HttpClient
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
	fun apiCaller(apiProperties: ApiProperties, builder: RestTemplateBuilder): ApiCaller {
		val restTemplate = builder
			.setConnectTimeout(Duration.ofSeconds(1))
			.setReadTimeout(Duration.ofSeconds(3))
			.build()

		val httpClient = HttpClient(restTemplate)

		return ApiCaller(httpClient, apiProperties)
	}

	@Bean
	fun fileUrlConverter(apiProperties: ApiProperties): FileUrlConverter {
		return FileUrlConverter(apiProperties)
	}
}
