package kim.hyunsub.video.controller

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.config.userAuth
import kim.hyunsub.common.web.model.SimpleResponse2
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.api.ApiVideoHistory
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.model.dto.VideoHistoryCreateParams
import kim.hyunsub.video.model.dto.VideoHistoryDeleteBulkParams
import kim.hyunsub.video.repository.VideoHistoryRepository
import kim.hyunsub.video.repository.entity.VideoHistory
import kim.hyunsub.video.repository.entity.VideoHistoryId
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class VideoHistoryController(
	private val videoHistoryRepository: VideoHistoryRepository,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("/api/v1/histories")
	fun list(
		user: UserAuth,
		@RequestParam category: String,
		@RequestParam(required = false, defaultValue = "0") p: Int,
		@RequestParam(required = false, defaultValue = "48") ps: Int,
	): ApiPageResult<ApiVideoHistory> {
		val userId = user.idNo
		val total = videoHistoryRepository.countByUserId(userId)

		val page = PageRequest.of(p, ps)
		val list = videoHistoryRepository.selectByUserId(user.idNo, category, page)

		return ApiPageResult(
			total = total,
			page = p,
			pageSize = ps,
			data = list.map { it.toApi() },
		)
	}

	@PostMapping("/api/v1/histories/delete-bulk")
	fun deleteBulk(
		user: UserAuth,
		@RequestBody params: VideoHistoryDeleteBulkParams,
	): SimpleResponse2 {
		val userId = user.idNo

		videoHistoryRepository.deleteAllById(
			params.videoIds.map {
				VideoHistoryId(userId, it)
			}
		)

		return SimpleResponse2()
	}

	@DeleteMapping("/api/v1/histories/:videoId")
	fun delete(
		user: UserAuth,
		@RequestParam videoId: String,
	): SimpleResponse2 {
		val userId = user.idNo

		videoHistoryRepository.deleteById(
			VideoHistoryId(
				userId = userId,
				videoId = videoId,
			)
		)

		return SimpleResponse2()
	}

	@MessageMapping("/v1/histories")
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
