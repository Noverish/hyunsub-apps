package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchMember
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchMemberMapper {
	fun selectList(tripId: String): List<DutchMember>
	fun deleteByTripId(tripId: String): Int
}
