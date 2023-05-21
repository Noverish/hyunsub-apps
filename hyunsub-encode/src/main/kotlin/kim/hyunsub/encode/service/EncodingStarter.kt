package kim.hyunsub.encode.service

import kim.hyunsub.common.fs.FsVideoClient
import kim.hyunsub.common.fs.model.FFmpegParams
import kim.hyunsub.encode.repository.EncodeRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.Executors

@Service
class EncodingStarter(
	private val encodeRepository: EncodeRepository,
	private val fsVideoClient: FsVideoClient,
) {
	private val executor = Executors.newSingleThreadScheduledExecutor()
	private val log = KotlinLogging.logger { }

	fun tryStartNext() {
		executor.execute {
			startEncoding()
		}
	}

	private fun startEncoding() {
		Thread.sleep(1000)

		if (encodeRepository.getNowEncoding() != null) {
			log.info { "[encode] Already running" }
			return
		}

		val candidate = encodeRepository.getCandidates().firstOrNull() ?: run {
			log.info { "[encode] No candidate" }
			return
		}
		log.info { "[encode] candidate=$candidate" }

		fsVideoClient.ffmpeg(
			FFmpegParams(
				input = candidate.input,
				options = candidate.options,
				output = candidate.encodeOutput,
			)
		)

		encodeRepository.saveAndFlush(candidate.copy(startDt = LocalDateTime.now()))
	}
}
