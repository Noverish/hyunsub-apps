package kim.hyunsub.encode.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumer(
	private val encodeService: EncodeService,
	private val encodeStatusService: EncodeStatusService,
) {
	@KafkaListener(topics = ["ffmpeg-out"])
	fun onOut(msg: String) {
		encodeStatusService.updateStatus(msg)
	}

	@KafkaListener(topics = ["ffmpeg-close"])
	fun onClose(code: String) {
		if (code == "0") {
			encodeService.handleFinish()
		} else {
			encodeService.handleError(code)
		}
	}
}
