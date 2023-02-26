package kim.hyunsub.video.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.repository.VideoHistoryRepository
import kim.hyunsub.video.repository.entity.VideoHistory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/histories")
class VideoHistoryController(
	private val videoHistoryRepository: VideoHistoryRepository,
) {
	@GetMapping("")
	fun list(user: UserAuth): List<VideoHistory> {
		return videoHistoryRepository.findByUserId(user.idNo)
	}
}
