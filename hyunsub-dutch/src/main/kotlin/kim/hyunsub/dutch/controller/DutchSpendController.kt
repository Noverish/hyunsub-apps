package kim.hyunsub.dutch.controller

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.dutch.bo.DutchSpendBo
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.repository.entity.DutchSpend
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}")
class DutchSpendController(
	private val dutchSpendBo: DutchSpendBo,
) {
	@GetMapping("/spends")
	fun list(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@RequestParam page: Int?,
	): ApiPageResult<DutchSpend> {
		return dutchSpendBo.list(memberAuth.memberId, page)
	}
}