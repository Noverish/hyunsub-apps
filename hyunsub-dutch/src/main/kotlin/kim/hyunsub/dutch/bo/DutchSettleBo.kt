package kim.hyunsub.dutch.bo

import kim.hyunsub.dutch.mapper.DutchSettleMapper
import kim.hyunsub.dutch.model.dto.DutchSettleParams
import kim.hyunsub.dutch.model.dto.DutchSettleResult
import kim.hyunsub.dutch.model.dto.DutchSettleResultShare
import kim.hyunsub.dutch.repository.entity.DutchSettleMemberTempResult
import org.springframework.stereotype.Service

@Service
class DutchSettleBo(
	private val dutchSettleMapper: DutchSettleMapper,
) {
	fun settle(tripId: String, params: DutchSettleParams): List<DutchSettleResult> {
		return dutchSettleMapper.settle(tripId)
			.groupBy { it.currency }
			.map {
				DutchSettleResult(
					currency = it.key,
					shares = settleByCurrency(it.value, params.mainMemberId)
				)
			}
	}

	private fun settleByCurrency(
		members: List<DutchSettleMemberTempResult>,
		mainMemberId: String,
	): List<DutchSettleResultShare> {
		return members
			.filter { it.memberId != mainMemberId }
			.map {
				DutchSettleResultShare(
					memberId = it.memberId,
					amount = it.should - it.actual,
				)
			}
	}
}
