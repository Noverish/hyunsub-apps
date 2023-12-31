package kim.hyunsub.dutch.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchRecordMapper
import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchRecord
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchRecordCreateParams
import kim.hyunsub.dutch.repository.DutchRecordMemberRepository
import kim.hyunsub.dutch.repository.DutchRecordRepository
import kim.hyunsub.dutch.repository.entity.DutchRecord
import kim.hyunsub.dutch.repository.entity.DutchRecordMember
import kim.hyunsub.dutch.repository.generateId
import kim.hyunsub.dutch.service.DutchMemberService
import kim.hyunsub.dutch.service.DutchRecordMemberService
import org.springframework.stereotype.Service

@Service
class DutchRecordMutateBo(
	private val dutchRecordRepository: DutchRecordRepository,
	private val dutchRecordMapper: DutchRecordMapper,
	private val dutchRecordMemberRepository: DutchRecordMemberRepository,
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
	private val dutchMemberService: DutchMemberService,
	private val dutchRecordMemberService: DutchRecordMemberService,
) {
	fun create(tripId: String, params: DutchRecordCreateParams): ApiDutchRecord {
		dutchMemberService.validateMemberIds(tripId, params.members.map { it.memberId })
		dutchRecordMemberService.validateRecordMember(params.members)

		val record = DutchRecord(
			id = dutchRecordRepository.generateId(),
			content = params.content,
			location = params.location,
			currency = params.currency,
			date = params.date,
			tripId = tripId,
		)

		val members = params.members.map {
			DutchRecordMember(
				recordId = record.id,
				memberId = it.memberId,
				actual = it.actual,
				should = it.should,
			)
		}

		dutchRecordRepository.save(record)
		dutchRecordMemberRepository.saveAll(members)

		return record.toApi()
	}

	fun delete(tripId: String, recordId: String): ApiDutchRecord {
		val record = dutchRecordMapper.selectOne(tripId, recordId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such record: $recordId")

		dutchRecordMemberMapper.deleteByRecordId(recordId)
		dutchRecordRepository.delete(record)

		return record.toApi()
	}
}
