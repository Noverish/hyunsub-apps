package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchMyBo
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.api.ApiDutchMember
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/my")
class DutchMyController(
	private val dutchMyBo: DutchMyBo,
) {
	@GetMapping("")
	fun my(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
	): ApiDutchMember? {
		return dutchMyBo.my(tripId, memberAuth.memberId)
	}
}
