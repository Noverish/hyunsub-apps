package kim.hyunsub.dutch.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import org.springframework.stereotype.Service

@Service
class DutchMemberService(
	private val dutchMemberMapper: DutchMemberMapper,
) {
	fun validateMemberIds(tripId: String, memberIds: List<String>) {
		val exists = dutchMemberMapper.selectByIds(memberIds)
			.map { it.id }
		val diff = memberIds - exists.toSet()
		if (diff.isNotEmpty()) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No such members: $diff")
		}
	}
}
