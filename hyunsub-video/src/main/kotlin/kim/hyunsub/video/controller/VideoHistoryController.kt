package kim.hyunsub.video.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.repository.VideoHistoryRepository
import kim.hyunsub.video.repository.entity.VideoMyHistory
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/histories")
class VideoHistoryController(
	private val videoHistoryRepository: VideoHistoryRepository,
) {
	@GetMapping("")
	fun list(
		user: UserAuth,
		@RequestParam(required = false, defaultValue = "0") p: Int,
		@RequestParam(required = false, defaultValue = "48") ps: Int,
	): RestApiPageResult<VideoMyHistory> {
		val userId = user.idNo
		val total = videoHistoryRepository.countByUserId(userId)

		val page = PageRequest.of(p, ps)
		val list = videoHistoryRepository.selectHistories(user.idNo, page)

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = ps,
			data = list
		)
	}
}
