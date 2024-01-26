package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchBalanceBo
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.api.ApiDutchBalance
import kim.hyunsub.dutch.model.dto.DutchBalanceCreateParams
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/balances")
class DutchBalanceController(
	private val dutchBalanceBo: DutchBalanceBo,
) {
	@GetMapping("")
	fun list(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
	): List<ApiDutchBalance> {
		return dutchBalanceBo.list(memberAuth.memberId)
	}

	@PostMapping("")
	fun create(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@RequestBody params: DutchBalanceCreateParams,
	): ApiDutchBalance {
		return dutchBalanceBo.create(memberAuth.memberId, params)
	}
}
