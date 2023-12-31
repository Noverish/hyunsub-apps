package kim.hyunsub.dutch.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.model.dto.DutchRecordMemberCreateParams
import org.springframework.stereotype.Service

@Service
class DutchRecordMemberService {
	fun validateRecordMember(members: List<DutchRecordMemberCreateParams>) {
		val actualSum = members.sumOf { it.actual }
		val shouldSum = members.sumOf { it.should }
		if (actualSum != shouldSum) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "invalid amount")
		}
	}
}
