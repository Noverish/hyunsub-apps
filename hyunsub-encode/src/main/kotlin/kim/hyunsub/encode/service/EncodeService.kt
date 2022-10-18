package kim.hyunsub.encode.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.FFmpegParams
import kim.hyunsub.encode.model.EncodeParams
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.repository.entity.Encode
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EncodeService(
	private val encodeRepository: EncodeRepository,
	private val apiCaller: ApiCaller,
) {
	val log = KotlinLogging.logger { }

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

		startIfNotRunning()
	}

	fun startIfNotRunning() {
		val candidate = encodeRepository.findByEndDtIsNull().minByOrNull { it.regDt } ?: run {
			log.info { "[encode] No candidate" }
			return
		}
		log.info { "[encode] candidate=$candidate" }

		if (candidate.startDt != null) {
			log.info { "[encode] Already running" }
			return
		}

		val encodeId = candidate.id
		log.info { "[encode] Start encode: $encodeId" }

		apiCaller.ffmpeg(
			FFmpegParams(
				input = candidate.input,
				options = candidate.options,
				output = candidate.encodeOutput,
				data = encodeId,
			)
		)

		encodeRepository.saveAndFlush(candidate.copy(startDt = LocalDateTime.now()))
	}

	fun handleFinish(encodeId: Int) {
		val encode = encodeRepository.findByIdOrNull(encodeId) ?: run {
			log.error { "[handleFinish] No such encodeId: $encodeId" }
			startIfNotRunning()
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

		startIfNotRunning()
	}
}
