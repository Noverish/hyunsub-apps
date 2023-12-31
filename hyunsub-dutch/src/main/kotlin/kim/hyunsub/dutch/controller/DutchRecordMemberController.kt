package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.repository.entity.DutchRecordMember
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
	): List<DutchRecordMember> {
		return dutchRecordMemberMapper.selectList(recordId)
	}

	@GetMapping("/{memberId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable recordId: String,
		@PathVariable memberId: String,
	): DutchRecordMember? {
		return dutchRecordMemberMapper.selectOne(recordId, memberId)
	}
}
