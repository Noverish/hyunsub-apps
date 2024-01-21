package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchSettleMemberTempResult
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchSettleMapper {
	fun settle(tripId: String): List<DutchSettleMemberTempResult>
}
