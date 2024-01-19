package kim.hyunsub.dutch.mapper

import kim.hyunsub.common.database.MapperBase
import kim.hyunsub.common.database.generateId
import kim.hyunsub.dutch.repository.entity.DutchMember
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchMemberMapper : MapperBase {
	override fun count(id: String): Int

	fun select(memberId: String, tripId: String? = null): DutchMember?
	fun selectByTripId(tripId: String): List<DutchMember>
	fun selectByIds(ids: List<String>): List<DutchMember>

	fun insertAll(entities: List<DutchMember>): Int

	fun delete(memberId: String, tripId: String? = null): Int
	fun deleteByTripId(tripId: String): Int
}

fun DutchMemberMapper.generateId() = generateId(8)
