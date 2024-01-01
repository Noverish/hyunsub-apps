package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchRecordMember
import kim.hyunsub.dutch.model.api.toApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/records/{recordId}/members")
class DutchRecordMemberController(
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable recordId: String,
	): List<ApiDutchRecordMember> {
		return dutchRecordMemberMapper.selectByRecordId(recordId)
			.map { it.toApi() }
	}

	@GetMapping("/{memberId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable recordId: String,
		@PathVariable memberId: String,
	): ApiDutchRecordMember? {
		return dutchRecordMemberMapper.selectOne(recordId, memberId)?.toApi()
	}
}
