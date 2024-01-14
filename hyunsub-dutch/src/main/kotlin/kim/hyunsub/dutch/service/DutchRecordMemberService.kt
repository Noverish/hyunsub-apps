package kim.hyunsub.dutch.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchRecordMember
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchRecordMemberParams
import kim.hyunsub.dutch.repository.entity.DutchRecordMember
import kim.hyunsub.dutch.repository.entity.DutchRecordMemberWithName
import org.springframework.stereotype.Service

@Service
class DutchRecordMemberService(
	private val dutchMemberMapper: DutchMemberMapper,
) {
	fun validateRecordMember(members: List<DutchRecordMemberParams>) {
		val actualSum = members.sumOf { it.actual }
		val shouldSum = members.sumOf { it.should }
		if (actualSum != shouldSum) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "invalid amount")
		}
	}

	fun convertToApi(list: List<DutchRecordMember>): List<ApiDutchRecordMember> {
		val memberIds = list.map { it.memberId }
		val memberNameMap = dutchMemberMapper.selectByIds(memberIds)
			.associate { it.id to it.name }

		return list.map {
			DutchRecordMemberWithName(
				recordId = it.recordId,
				memberId = it.memberId,
				actual = it.actual,
				should = it.should,
				regDt = it.regDt,
				name = memberNameMap[it.memberId] ?: "",
			).toApi()
		}
	}
}
