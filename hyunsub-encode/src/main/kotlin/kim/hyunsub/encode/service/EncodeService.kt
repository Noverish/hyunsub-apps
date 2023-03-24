package kim.hyunsub.encode.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.EncodeParams
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.repository.entity.Encode
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EncodeService(
	private val encodeRepository: EncodeRepository,
	private val apiCaller: ApiCaller,
	private val encodingStarter: EncodingStarter,
) {
	private val log = KotlinLogging.logger { }

	fun pushToQueue(params: EncodeParams) {
		val encode = Encode(
			input = params.input,
			options = params.options,
			output = params.output,
			regDt = LocalDateTime.now(),
			callback = params.callback,
		)
		log.info { "[pushToQueue] $encode" }
		encodeRepository.saveAndFlush(encode)

		encodingStarter.tryStartNext()
	}

	fun handleFinish() {
		val encode = encodeRepository.getNowEncoding() ?: run {
			log.error { "[handleFinish] Nothing is currently being encoded" }
			encodingStarter.tryStartNext()
			return
		}
		log.info { "[handleFinish] $encode" }

		val newEncode = encode.copy(endDt = LocalDateTime.now(), progress = 100)
		encodeRepository.saveAndFlush(newEncode)

		if (encode.input == encode.output) {
			apiCaller.rename(encode.input, encode.input + ".old")
			apiCaller.rename(encode.encodeOutput, encode.input)
			apiCaller.copyMDate(encode.input + ".old", encode.output)
		} else {
			apiCaller.copyMDate(encode.input, encode.output)
		}

		encode.callback
			?.let { apiCaller.get(it) }
			?.let { log.info { "[handleFinish] callback result: $it" } }

		encodingStarter.tryStartNext()
	}

	fun handleError(code: String) {
		log.error { "[handleError] code=$code" }

		val encode = encodeRepository.getNowEncoding() ?: run {
			log.error { "[handleError] Nothing is currently being encoded" }
			encodingStarter.tryStartNext()
			return
		}
		log.info { "[handleError] $encode" }

		val newEncode = encode.copy(endDt = LocalDateTime.now(), progress = -1)
		encodeRepository.saveAndFlush(newEncode)

		encodingStarter.tryStartNext()
	}
}
