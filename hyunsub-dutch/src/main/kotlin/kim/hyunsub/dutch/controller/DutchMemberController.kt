package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchMember
import kim.hyunsub.dutch.model.api.toApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/members")
class DutchMemberController(
	private val dutchMemberMapper: DutchMemberMapper,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable tripId: String,
	): List<ApiDutchMember> {
		return dutchMemberMapper.selectList(tripId).map { it.toApi() }
	}
}
