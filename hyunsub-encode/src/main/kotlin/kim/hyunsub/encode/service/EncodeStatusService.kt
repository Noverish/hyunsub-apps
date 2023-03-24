package kim.hyunsub.encode.service

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class EncodeStatusService {
	val log = KotlinLogging.logger { }

	fun updateStatus(msg: String) {
		log.info { msg }
	}
}
