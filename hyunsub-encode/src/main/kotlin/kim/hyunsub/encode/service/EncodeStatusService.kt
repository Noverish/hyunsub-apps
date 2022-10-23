package kim.hyunsub.encode.service

import com.fasterxml.jackson.databind.node.ObjectNode
import kim.hyunsub.common.api.model.FFmpegStatus
import kim.hyunsub.encode.model.EncodeStatus
import kim.hyunsub.encode.repository.EncodeRepository
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import reactor.core.publisher.Sinks
import kotlin.math.floor

@Service
class EncodeStatusService(
	private val encodeRepository: EncodeRepository,
	private val cacheService: VideoMetadataCacheService,
	private val sink: Sinks.Many<EncodeStatus>,
) {
	val log = KotlinLogging.logger { }

	fun updateStatus(status: FFmpegStatus<Int>) {
		val encodeId = status.data

		val encode = encodeRepository.findByIdOrNull(encodeId)
			?: throw IllegalArgumentException("No such encode id: $encodeId")

		val probed = cacheService.getOrFetch(encode.input)
		val format = probed["format"] as ObjectNode
		val duration = format["duration"].asDouble()

		val percent =
			if (status.isRunning)
				floor(status.status.progress.toDouble() / duration * 10000) / 100
			else
				100.0
		val encodeStatus = EncodeStatus(encodeId, percent)
		log.info { "[EncodeStatus] $encodeStatus" }
		sink.tryEmitNext(encodeStatus)
	}
}
