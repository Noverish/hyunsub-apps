package kim.hyunsub.encode.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.FFmpegParams
import kim.hyunsub.common.log.Log
import kim.hyunsub.encode.model.EncodeParams
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.repository.entity.Encode
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EncodeService(
	private val encodeRepository: EncodeRepository,
	private val apiCaller: ApiCaller,
) {
	companion object : Log

	fun encode(params: EncodeParams) {
		val newEncode = Encode(
			input = params.input,
			options = params.options,
			output = params.output,
			regDt = LocalDateTime.now(),
		)
		log.debug("[EncodeService] encode: newEncode={}", newEncode)
		encodeRepository.saveAndFlush(newEncode)

		EncodeThread(encodeRepository, apiCaller).start()
	}
}
