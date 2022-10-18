package kim.hyunsub.encode.service

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer {
	val log = KotlinLogging.logger { }

	@KafkaListener(topics = ["encode"])
	fun consume(msg: String) {
		log.info { "KafkaConsumer: $msg" }
	}
}
