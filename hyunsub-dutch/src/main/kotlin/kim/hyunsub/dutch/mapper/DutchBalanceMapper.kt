package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.repository.entity.DutchBalance
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchBalanceMapper {
	fun select(memberId: String, currency: DutchCurrency): DutchBalance?
	fun selectByMemberId(memberId: String): List<DutchBalance>

	fun insert(entity: DutchBalance): Int
	fun update(entity: DutchBalance): Int
}
