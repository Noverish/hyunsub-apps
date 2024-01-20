package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchTripBo
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.api.ApiDutchTrip
import kim.hyunsub.dutch.model.dto.DutchTripCreateParams
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
	private val dutchTripBo: DutchTripBo,
) {
	@GetMapping("/{tripId}")
	fun detail(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
	): ApiDutchTrip? {
		return dutchTripBo.delete(tripId)
	}

	@PostMapping("")
	fun create(
		memberAuth: DutchMemberAuth,
		@RequestBody params: DutchTripCreateParams,
	): ApiDutchTrip {
		return dutchTripBo.create(params)
	}

	@DeleteMapping("/{tripId}")
	fun delete(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
	): ApiDutchTrip {
		return dutchTripBo.delete(tripId)
	}
}
