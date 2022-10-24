package kim.hyunsub.division.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.division.repository.GatheringRepository
import kim.hyunsub.division.repository.entity.Gathering
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/gathering")
class GatheringController(
	private val gatheringRepository: GatheringRepository,
) {
	@GetMapping("/{gatheringId}")
	fun get(@PathVariable gatheringId: String): Gathering {
		return gatheringRepository.findByIdOrNull(gatheringId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
	}
}
