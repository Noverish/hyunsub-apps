package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchTripCurrency
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchTripCurrencyMapper {
	fun selectByTripId(tripId: String): List<DutchTripCurrency>
	fun upsert(entity: DutchTripCurrency): Int
}
