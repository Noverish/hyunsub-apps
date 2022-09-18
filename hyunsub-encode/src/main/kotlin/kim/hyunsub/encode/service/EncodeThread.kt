package kim.hyunsub.encode.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.FFmpegParams
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.log.hashWithMD5
import kim.hyunsub.encode.repository.EncodeRepository
import kim.hyunsub.encode.repository.entity.Encode
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.extension

class EncodeThread(
	private val encodeRepository: EncodeRepository,
	private val apiCaller: ApiCaller,
	private val resume: Boolean = false,
) : Thread() {
	companion object : Log

	override fun run() {
		try {
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
				log.info("[EncodeThread] percent={}", percent)

				encodeRepository.findByIdOrNull(candidate.id)!!
					.copy(progress = percent)
					.let { encodeRepository.saveAndFlush(it) }
			}

			log.info("[EncodeThread] Finished")

			val result = encodeRepository.findByIdOrNull(candidate.id)!!
				.copy(endDt = LocalDateTime.now(), progress = 100)
			encodeRepository.saveAndFlush(result)

			if (result.input == result.output) {
				log.info("[EncodeThread] rename: result={}", result)
				apiCaller.rename(result.input, result.input + ".old")
				apiCaller.rename(getRealOutputPath(result), result.input)
				apiCaller.copyMDate(result.input + ".old", result.output)
			} else {
				apiCaller.copyMDate(result.input, result.output)
			}

			result.callback
				?.let { apiCaller.get(it) }
				?.let { log.info("[EncodeThread] callback={}", it) }

			EncodeThread(encodeRepository, apiCaller).start()
		} catch (ex: Exception) {
			log.error(ex.message, ex)
		}
	}

	private fun startEncode(candidate: Encode) {
		val output = getRealOutputPath(candidate)
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

	private fun getRealOutputPath(encode: Encode): String {
		if (encode.input != encode.output) {
			return encode.output
		}

		val hash = encode.input.hashWithMD5()
		val path = Path(encode.input)
		val ext = path.extension
		val dir = path.parent.toString()
		return Path(dir, "$hash.$ext").toString()
	}
}
