package kim.hyunsub.common.fs.config

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Logger
import feign.jackson.JacksonDecoder
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.FeignBuilderCustomizer
import org.springframework.cloud.openfeign.FeignLoggerFactory
import org.springframework.cloud.openfeign.support.SpringMvcContract
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackages = ["kim.hyunsub.common.fs.client"])
@ImportAutoConfiguration(classes = [FeignAutoConfiguration::class, HttpClientConfiguration::class]) // 자동으로 auto configuration 되지 않아 명시적으로 선언
@EnableConfigurationProperties(FsProperties::class)
class FsConfiguration {
	@Bean
	fun feignLoggerFactory(): FeignLoggerFactory {
		return FeignLoggerFactory { FsClientLogger(it) }
	}

	@Bean
	fun feignBuilderCustomizer(mapper: ObjectMapper, interceptor: FsClientInterceptor) =
		FeignBuilderCustomizer {
			it
				.decoder(JacksonDecoder(mapper))
				.contract(SpringMvcContract())
				.logLevel(Logger.Level.FULL)
				.requestInterceptor(interceptor)
		}
}
