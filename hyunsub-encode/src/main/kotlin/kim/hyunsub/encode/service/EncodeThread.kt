package kim.hyunsub.encode.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.FFmpegParams
import kim.hyunsub.common.http.HttpClient
import kim.hyunsub.common.log.Log
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.repository.entity.Encode
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

class EncodeThread(
	private val encodeRepository: EncodeRepository,
	private val apiCaller: ApiCaller,
	private val httpClient: HttpClient,
	private val resume: Boolean = false,
) : Thread() {
	companion object : Log

	override fun run() {
		log.info("[EncodeThread resume={}", resume)

		val candidate = encodeRepository.findByEndDtIsNull().minByOrNull { it.regDt }
		if (candidate == null) {
			log.info("[EncodeThread] No candidate")
			return
		}

		log.info("[EncodeThread] candidate={}", candidate)
		if (candidate.startDt != null) {
			if (!resume) {
				log.info("[EncodeThread] Candidate is already in progress")
				return
			}
		} else {
			startEncode(candidate)
		}

		val ffprobe = apiCaller.ffprobe(candidate.input)
		val duration = ffprobe["format"]["duration"].asInt()
		log.debug("[EncodeThread] duration={}", duration)

		while (true) {
			sleep(3000)

			val status = apiCaller.ffmpegStatus().status
			log.debug("[EncodeThread] status={}", status)
			if (status.q == "-1.0") {
				break
			}

			val progress = status.progress
			val percent = (progress.toDouble() / duration.toDouble() * 100).toInt()
			log.debug("[EncodeThread] percent={}", percent)

			encodeRepository.findByIdOrNull(candidate.id)!!
				.let { it.copy(progress = percent) }
				.let { encodeRepository.saveAndFlush(it) }
		}

		val result = encodeRepository.findByIdOrNull(candidate.id)!!
			.copy(endDt = LocalDateTime.now(), progress = 100)
		encodeRepository.saveAndFlush(result)

		if (result.output == null && result.input.endsWith(".mp4", true)) {
			apiCaller.rename(result.input, result.input + ".old")
			apiCaller.rename(generateOutput(result.input), result.input)
		}

		result.callback?.let { httpClient.get<String>(it) }

		EncodeThread(encodeRepository, apiCaller, httpClient).start()
	}

	private fun startEncode(candidate: Encode) {
		val output = candidate.output ?: generateOutput(candidate.input)
		apiCaller.ffmpeg(
			FFmpegParams(
				input = candidate.input,
				options = candidate.options,
				output = output,
			)
		)

		candidate.copy(startDt = LocalDateTime.now())
			.let { encodeRepository.saveAndFlush(it) }
	}

	private fun generateOutput(input: String): String {
		if (input.endsWith(".mp4", true)) {
			return "$input.mp4"
		}

		val ext = Path(input).extension
		return input.replace(Regex("$ext$"), "mp4")
	}
}
