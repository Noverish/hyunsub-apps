package kim.hyunsub.encode.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConsumerConfiguration {
	private fun generateConsumerFactory(): ConsumerFactory<String, String> {
		val map = buildMap<String, Any> {
			this[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "192.168.0.20:9092"
			this[ConsumerConfig.GROUP_ID_CONFIG] = "hyunsub"
			this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
			this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
		}
		return DefaultKafkaConsumerFactory(map)
	}

	@Bean
	fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
		val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
		factory.consumerFactory = generateConsumerFactory()
		return factory
	}
}
