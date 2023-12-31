package kim.hyunsub.dutch.bo

import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.model.dto.DutchSettleMemberResult
import kim.hyunsub.dutch.model.dto.DutchSettleParams
import kim.hyunsub.dutch.model.dto.DutchSettleResult
import org.springframework.stereotype.Service

@Service
class DutchSettleBo(
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
) {
	fun settle(tripId: String, params: DutchSettleParams): DutchSettleResult {
		val members = dutchRecordMemberMapper.settle(tripId)
			.filter { it.memberId != params.mainMemberId }
			.map {
				DutchSettleMemberResult(
					memberId = it.memberId,
					amount = it.should - it.actual,
				)
			}

		return DutchSettleResult(members)
	}
}
