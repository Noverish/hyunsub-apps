package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchSettleBo
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.dto.DutchSettleParams
import kim.hyunsub.dutch.model.dto.DutchSettleResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/settle")
class DutchSettleController(
	private val dutchSettleBo: DutchSettleBo,
) {
	@GetMapping("/each")
	fun settleEach(
		@PathVariable tripId: String,
	): List<DutchSettleResult> {
		return dutchSettleBo.settleEach(tripId)
	}

	@PostMapping("")
	fun settle(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@RequestBody params: DutchSettleParams,
	): Map<String, Int> {
		return dutchSettleBo.settle(tripId, params)
	}
}