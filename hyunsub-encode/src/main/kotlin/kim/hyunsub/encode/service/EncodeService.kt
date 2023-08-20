package kim.hyunsub.encode.service

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.copyMDate
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.fs.model.EncodeParams
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.repository.entity.Encode
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EncodeService(
	private val encodeRepository: EncodeRepository,
	private val encodingStarter: EncodingStarter,
	private val fsClient: FsClient,
	private val encodeCallbackService: EncodeCallbackService,
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
			fsClient.rename(encode.input, encode.input + ".old")
			fsClient.rename(encode.encodeOutput, encode.input)
			fsClient.copyMDate(encode.input + ".old", encode.output)
		} else {
			fsClient.copyMDate(encode.input, encode.output)
		}

		encode.callback
			?.let { encodeCallbackService.sendCallback(it) }
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
