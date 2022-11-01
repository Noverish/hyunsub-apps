package kim.hyunsub.division.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.division.repository.GatheringRepository
import kim.hyunsub.division.repository.GatheringUserRepository
import kim.hyunsub.division.repository.entity.Gathering
import kim.hyunsub.division.service.GatheringService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/gatherings")
class GatheringController(
	private val gatheringRepository: GatheringRepository,
	private val gatheringUserRepository: GatheringUserRepository,
	private val gatheringService: GatheringService,
) {
	val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(userAuth: UserAuth): List<Gathering> {
		log.debug { "[Gathering List] idNo=${userAuth.idNo}" }

		val gatheringIds = gatheringUserRepository.findByUserId(userAuth.idNo)
			.map { it.gatheringId }
		log.debug { "[Gathering List] gatheringIds=$gatheringIds" }

		val gatherings = gatheringRepository.findAllById(gatheringIds)
		log.debug { "[Gathering List] gatherings=$gatherings" }

		return gatherings
	}

	@GetMapping("/{gatheringId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable gatheringId: String
	): Gathering {
		return gatheringService.detail(gatheringId, userAuth.idNo)
	}
}
