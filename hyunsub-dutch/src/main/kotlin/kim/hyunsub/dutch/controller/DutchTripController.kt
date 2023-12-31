package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.bo.DutchTripMutateBo
import kim.hyunsub.dutch.model.api.ApiDutchTrip
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchTripCreateParams
import kim.hyunsub.dutch.repository.DutchTripRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips")
class DutchTripController(
	private val dutchTripRepository: DutchTripRepository,
	private val dutchTripMutateBo: DutchTripMutateBo,
) {
	@GetMapping("/{tripId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable tripId: String,
	): ApiDutchTrip? {
		return dutchTripRepository.findByIdOrNull(tripId)?.toApi()
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: DutchTripCreateParams,
	): ApiDutchTrip {
		return dutchTripMutateBo.create(params)
	}

	@DeleteMapping("/{tripId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable tripId: String,
	): ApiDutchTrip {
		return dutchTripMutateBo.delete(tripId)
	}
}
