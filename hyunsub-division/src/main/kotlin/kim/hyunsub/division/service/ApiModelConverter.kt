package kim.hyunsub.division.service

import kim.hyunsub.division.model.dto.RestApiGatheringUser
import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.model.dto.RestApiRecordShare
import kim.hyunsub.division.repository.entity.Record
import kim.hyunsub.division.repository.entity.RecordShare
import kim.hyunsub.division.repository.entity.User
import org.springframework.stereotype.Service

@Service
class ApiModelConverter {
	val recordMapper = RecordMapper.mapper
	val recordShareMapper = RecordShareMapper.mapper

	fun convert(record: Record, shares: List<RecordShare>): RestApiRecord {
		return recordMapper.convert(record).copy(
			shares = shares.map { convert(it) },
		)
	}

	fun convert(share: RecordShare): RestApiRecordShare {
		return recordShareMapper.convert(share)
	}

	fun convert(user: User): RestApiGatheringUser {
		return RestApiGatheringUser(
			userId = user.idNo,
			username = user.username
		)
	}
}
