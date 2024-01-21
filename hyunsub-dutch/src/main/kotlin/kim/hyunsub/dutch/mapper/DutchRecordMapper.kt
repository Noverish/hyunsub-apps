package kim.hyunsub.dutch.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.common.database.generateId
import kim.hyunsub.dutch.model.DutchCurrency
import kim.hyunsub.dutch.model.dto.DutchRecordSearchParams
import kim.hyunsub.dutch.repository.entity.DutchRecord
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchRecordMapper : MapperBase {
	override fun count(id: String): Int

	fun select(tripId: String, recordId: String): DutchRecord?
	fun selectCurrency(tripId: String): List<DutchCurrency>

	fun searchCount(params: DutchRecordSearchParams): Int
	fun search(params: DutchRecordSearchParams): List<DutchRecord>

	fun insert(entity: DutchRecord): Int
	fun update(entity: DutchRecord): Int
	fun delete(entity: DutchRecord): Int
	fun deleteByTripId(tripId: String): Int
}

fun DutchRecordMapper.generateId() = generateId(10)
