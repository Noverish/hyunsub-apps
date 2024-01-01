package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchMember
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchMemberMapper {
	fun selectByTripId(tripId: String): List<DutchMember>
	fun selectByIds(ids: List<String>): List<DutchMember>

	fun deleteByTripId(tripId: String): Int
}
