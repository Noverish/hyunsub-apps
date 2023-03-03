package kim.hyunsub.common.http

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class HttpClientConfiguration {
	@Bean
	fun httpClient(builder: RestTemplateBuilder): HttpClient {
		val restTemplate = builder
			.setConnectTimeout(Duration.ofSeconds(1))
			.setReadTimeout(Duration.ofSeconds(3))
			.build()
		return HttpClient(restTemplate)
	}
}
