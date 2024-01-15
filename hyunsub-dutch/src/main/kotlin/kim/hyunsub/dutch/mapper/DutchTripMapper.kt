package kim.hyunsub.dutch.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.dutch.repository.entity.DutchTrip
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchTripMapper : MapperBase {
	override fun count(id: String): Int

	fun select(tripId: String): DutchTrip?
}
