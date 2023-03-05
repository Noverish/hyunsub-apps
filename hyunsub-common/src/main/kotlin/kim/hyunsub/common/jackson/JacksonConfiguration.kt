package kim.hyunsub.common.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Configuration
class JacksonConfiguration {
	@Bean
	@Primary
	fun objectMapper(): ObjectMapper {
		val module = SimpleModule().apply {
			addSerializer(OffsetDateTime::class.java, OffsetDateTimeSerializer())
			addDeserializer(OffsetDateTime::class.java, OffsetDateTimeDeserializer())
			addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer())
			addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer())
			addSerializer(LocalDate::class.java, LocalDateSerializer())
			addDeserializer(LocalDate::class.java, LocalDateDeserializer())
		}

		return jacksonObjectMapper().apply {
			registerModule(module)
			configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		}
	}
}
