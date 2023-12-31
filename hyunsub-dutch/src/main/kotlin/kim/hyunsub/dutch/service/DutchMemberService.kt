package kim.hyunsub.dutch.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.repository.DutchMemberRepository
import org.springframework.stereotype.Service

@Service
class DutchMemberService(
	private val dutchMemberRepository: DutchMemberRepository,
) {
	fun validateMemberIds(tripId: String, memberIds: List<String>) {
		val exists = dutchMemberRepository.findAllById(memberIds)
			.map { it.id }
		val diff = memberIds - exists.toSet()
		if (diff.isNotEmpty()) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No such members: $diff")
		}
	}
}
