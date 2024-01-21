package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchTripCurrencyBo
import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.api.ApiDutchTripCurrency
import kim.hyunsub.dutch.model.dto.DutchTripCurrencyUpdateParams
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/currencies")
class DutchTripCurrencyController(
	private val dutchTripCurrencyBo: DutchTripCurrencyBo,
) {
	@GetMapping("")
	fun list(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
	): List<ApiDutchTripCurrency> {
		return dutchTripCurrencyBo.list(tripId)
	}

	@PutMapping("/{currency}")
	fun set(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@PathVariable currency: DutchCurrency,
		@RequestBody params: DutchTripCurrencyUpdateParams,
	): ApiDutchTripCurrency {
		return dutchTripCurrencyBo.set(tripId, currency, params)
	}
}
