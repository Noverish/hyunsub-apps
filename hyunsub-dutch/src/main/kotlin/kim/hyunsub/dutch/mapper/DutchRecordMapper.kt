package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchRecord
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchRecordMapper {
	fun selectList(tripId: String): List<DutchRecord>
	fun selectOne(tripId: String, recordId: String): DutchRecord?
	fun deleteByTripId(tripId: String): Int
}
