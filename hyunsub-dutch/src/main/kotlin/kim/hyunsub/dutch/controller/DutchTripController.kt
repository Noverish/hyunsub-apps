package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.repository.DutchTripRepository
import kim.hyunsub.dutch.repository.entity.DutchTrip
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips")
class DutchTripController(
	private val dutchTripRepository: DutchTripRepository,
) {
	@GetMapping("/{tripId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable tripId: String,
	): DutchTrip? {
		return dutchTripRepository.findByIdOrNull(tripId)
	}
}
