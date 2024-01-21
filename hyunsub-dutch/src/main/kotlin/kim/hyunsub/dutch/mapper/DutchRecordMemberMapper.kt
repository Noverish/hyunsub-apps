package kim.hyunsub.dutch.mapper

import kim.hyunsub.dutch.repository.entity.DutchRecordMember
import kim.hyunsub.dutch.repository.entity.DutchRecordMemberWithName
import org.apache.ibatis.annotations.Mapper

@Mapper
interface DutchRecordMemberMapper {
	fun selectByRecordId(recordId: String): List<DutchRecordMemberWithName>
	fun selectByRecordIds(recordIds: List<String>): List<DutchRecordMemberWithName>
	fun selectOne(recordId: String, memberId: String): DutchRecordMemberWithName?

	fun insertAll(entities: List<DutchRecordMember>): Int
	fun deleteByTripId(tripId: String): Int
	fun deleteByRecordId(recordId: String): Int
}
