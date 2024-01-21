package kim.hyunsub.dutch.controller

import kim.hyunsub.dutch.bo.DutchMemberBo
import kim.hyunsub.dutch.config.DutchIgnoreAuthorize
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.model.api.ApiDutchMember
import kim.hyunsub.dutch.model.dto.DutchMemberCreateParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/members")
class DutchMemberController(
	private val dutchMemberBo: DutchMemberBo,
) {
	@DutchIgnoreAuthorize
	@GetMapping("")
	fun list(
		@PathVariable tripId: String,
	): List<ApiDutchMember> {
		return dutchMemberBo.list(tripId)
	}

	@PostMapping("")
	fun create(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@RequestBody params: DutchMemberCreateParams,
	): ApiDutchMember {
		return dutchMemberBo.create(tripId, params)
	}

	@DeleteMapping("/{memberId}")
	fun delete(
		memberAuth: DutchMemberAuth,
		@PathVariable tripId: String,
		@PathVariable memberId: String,
	): ApiDutchMember {
		return dutchMemberBo.delete(tripId, memberId)
	}
}
