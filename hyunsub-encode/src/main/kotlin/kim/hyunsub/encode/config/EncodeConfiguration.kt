package kim.hyunsub.encode.config

import kim.hyunsub.encode.model.EncodeStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Sinks

@Configuration
class EncodeConfiguration {
	@Bean
	fun statusSink(): Sinks.Many<EncodeStatus> {
		return Sinks.many().replay().latest()
	}
}
