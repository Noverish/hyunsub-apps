package kim.hyunsub.encode.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.api.model.FFmpegStatus
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
	private val encodeService: EncodeService,
	private val encodeStatusService: EncodeStatusService,
) {
	val log = KotlinLogging.logger { }
	val mapper = jacksonObjectMapper()
	val type = object : TypeReference<FFmpegStatus<Int>>() {}

	@KafkaListener(topics = ["encode"])
	fun consume(msg: String) {
		log.info { msg }
		val status = mapper.readValue(msg, type)

		if (!status.isRunning) {
			val encodeId = status.data
			encodeService.handleFinish(encodeId)
		} else {
			encodeStatusService.updateStatus(status)
		}
	}
}
