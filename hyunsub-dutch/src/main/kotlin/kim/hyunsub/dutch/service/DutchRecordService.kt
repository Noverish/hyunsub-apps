package kim.hyunsub.dutch.service

import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchRecord
import kim.hyunsub.dutch.repository.entity.DutchRecord
import org.springframework.stereotype.Service

@Service
class DutchRecordService(
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
) {
	fun convertToApi(record: DutchRecord): ApiDutchRecord {
		val memberList = dutchRecordMemberMapper.selectByRecordId(record.id)
		val amount = memberList.sumOf { it.actual }
		val members = memberList.map { it.name }

		return ApiDutchRecord(
			id = record.id,
			content = record.content,
			location = record.location,
			currency = record.currency,
			date = record.date,
			amount = amount,
			members = members,
		)
	}

	fun convertToApi(records: List<DutchRecord>): List<ApiDutchRecord> {
		val recordIds = records.map { it.id }
		val memberMap = dutchRecordMemberMapper.selectByRecordIds(recordIds)
			.groupBy { it.recordId }

		return records.map { record ->
			val memberList = memberMap[record.id] ?: emptyList()
			val amount = memberList.sumOf { it.actual }
			val members = memberList.map { it.name }

			ApiDutchRecord(
				id = record.id,
				content = record.content,
				location = record.location,
				currency = record.currency,
				date = record.date,
				amount = amount,
				members = members,
			)
		}
	}
}
