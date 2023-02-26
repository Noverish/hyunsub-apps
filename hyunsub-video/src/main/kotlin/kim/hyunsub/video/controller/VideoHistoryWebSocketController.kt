package kim.hyunsub.video.controller

import kim.hyunsub.common.web.config.userAuth
import kim.hyunsub.video.model.dto.VideoHistoryCreateParams
import kim.hyunsub.video.repository.VideoHistoryRepository
import kim.hyunsub.video.repository.entity.VideoHistory
import mu.KotlinLogging
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.time.LocalDateTime

@Controller
class VideoHistoryWebSocketController(
	private val videoHistoryRepository: VideoHistoryRepository,
) {
	private val log = KotlinLogging.logger { }

	@MessageMapping("/video/history")
	fun videoHistory(params: VideoHistoryCreateParams, accessor: SimpMessageHeaderAccessor) {
		if (params.time == 0) {
			return
		}

		val userAuth = accessor.userAuth

		val history = VideoHistory(
			userId = userAuth.idNo,
			videoId = params.videoId,
			time = params.time,
			date = LocalDateTime.now(),
		)

		log.debug { "[Create Video History] $history" }

		videoHistoryRepository.save(history)
	}
}
