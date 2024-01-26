package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchBalanceBo
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.api.ApiDutchBalance
import kim.hyunsub.dutch.model.dto.DutchBalanceUpdateBulkParams
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}")
class DutchBalanceController(
	private val dutchBalanceBo: DutchBalanceBo,
) {
	@GetMapping("/balances")
	fun list(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
	): List<ApiDutchBalance> {
		return dutchBalanceBo.list(memberAuth.memberId)
	}

	@PutMapping("/_bulk/balances")
	fun updateBulk(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@RequestBody params: DutchBalanceUpdateBulkParams,
	): List<ApiDutchBalance> {
		return dutchBalanceBo.updateBulk(memberAuth.memberId, params)
	}
}
