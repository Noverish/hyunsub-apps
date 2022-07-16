package kim.hyunsub.common.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDateTime

@Configuration
class JacksonConfiguration {
	@Bean
	@Primary
	fun objectMapper(): ObjectMapper {
		val module = SimpleModule().apply {
			addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
			addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
		}

		return jacksonObjectMapper().apply {
			registerModule(module)
		}
	}
}
