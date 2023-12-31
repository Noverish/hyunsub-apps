package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchRecordMember
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchRecordMemberMapper {
	fun selectList(recordId: String): List<DutchRecordMember>
	fun selectOne(recordId: String, memberId: String): DutchRecordMember?
	fun deleteByTripId(tripId: String): Int
	fun deleteByRecordId(recordId: String): Int
}
