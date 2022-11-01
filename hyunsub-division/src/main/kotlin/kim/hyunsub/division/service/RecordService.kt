package kim.hyunsub.division.service

import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.repository.GatheringRepository
import kim.hyunsub.division.repository.RecordRepository
import kim.hyunsub.division.repository.RecordShareRepository
import kim.hyunsub.division.repository.entity.Record
import kim.hyunsub.division.repository.entity.RecordShare
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class RecordService(
	private val gatheringRepository: GatheringRepository,
	private val recordRepository: RecordRepository,
	private val recordShareRepository: RecordShareRepository,
	private val randomGenerator: RandomGenerator,
	private val apiModelConverter: ApiModelConverter,
	private val gatheringService: GatheringService,
) {
	val log = KotlinLogging.logger { }
	val recordMapper: RecordMapper = RecordMapper.mapper
	val shareMapper: RecordShareMapper = RecordShareMapper.mapper

	fun list(gatheringId: String): List<RestApiRecord> {
		log.debug { "[Record List] gatheringId=$gatheringId" }

		val gathering = gatheringRepository.findByIdOrNull(gatheringId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Record List] gathering=$gathering" }

		val records = recordRepository.findByGatheringId(gathering.id)
		log.debug { "[Record List] records=$records" }
		val recordIds = records.map { it.id }

		val shares = recordShareRepository.findByRecordIdIn(recordIds)
		val shareMap = shares.groupBy { it.recordId }
		log.debug { "[Record List] shareMap=$shareMap" }

		return records.map {
			apiModelConverter.convert(it, shareMap[it.id] ?: emptyList())
		}
	}

	fun detail(
		recordId: String,
		userId: String? = null,
		gatheringId: String? = null,
	): RestApiRecord {
		log.debug { "[Record Detail] recordId=$recordId" }

		val record = recordRepository.findByIdOrNull(recordId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Record Detail] record=$record" }

		if (userId != null) {
			gatheringService.detail(record.gatheringId, userId)
		}

		if (gatheringId != null && gatheringId != record.gatheringId) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		val shares = recordShareRepository.findByRecordId(recordId)
		log.debug { "[Record Detail] shares=$shares" }

		return apiModelConverter.convert(record, shares)
	}

	fun create(
		params: RestApiRecord,
		userId: String? = null,
	): RestApiRecord {
		log.debug { "[Record Create] params=$params" }

		if (userId != null) {
			gatheringService.detail(params.gatheringId, userId)
		}

		val record = recordMapper.convert(params).copy(
			id = Record.generateId(recordRepository, randomGenerator),
		)
		log.debug { "[Record Create] record=$record" }
		recordRepository.save(record)

		val shares = params.shares.map {
			shareMapper.convert(it, record.id).copy(
				id = RecordShare.generateId(recordShareRepository, randomGenerator),
			)
		}
		log.debug { "[Record Create] shares=$shares" }
		validateAmount(shares)
		recordShareRepository.saveAll(shares)

		return apiModelConverter.convert(record, shares)
	}

	fun update(
		params: RestApiRecord,
		userId: String? = null,
	): RestApiRecord {
		log.debug { "[Record Update] params=$params" }

		if (userId != null) {
			gatheringService.detail(params.gatheringId, userId)
		}

		val record = recordMapper.convert(params)
		log.debug { "[Record Update] record=$record" }
		recordRepository.save(record)

		val dbShares = recordShareRepository.findByRecordId(record.id)
		log.debug { "[Record Update] dbShares=$dbShares" }

		val paramShares = params.shares.map { shareMapper.convert(it, record.id) }
		log.debug { "[Record Update] paramShares=$paramShares" }

		val paramShareUserIds = paramShares.map { it.userId }
		val deletes = dbShares.filter { it.userId !in paramShareUserIds }
		log.debug { "[Record Update] deletes=$deletes" }

		validateAmount(paramShares)
		recordShareRepository.deleteAll(deletes)
		recordShareRepository.saveAll(paramShares)

		return apiModelConverter.convert(record, paramShares)
	}

	fun delete(
		recordId: String,
		userId: String? = null,
		gatheringId: String? = null,
	) {
		log.debug { "[Record Delete] recordId=$recordId, userId=$userId" }

		val record = recordRepository.findByIdOrNull(recordId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Record Delete] record=$record" }

		if (userId != null) {
			gatheringService.detail(record.gatheringId, userId)
		}

		if (gatheringId != null && gatheringId != record.gatheringId) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		val shares = recordShareRepository.findByRecordId(recordId)
		log.debug { "[Record Delete] shares=$shares" }

		recordRepository.delete(record)
		recordShareRepository.deleteAll(shares)
	}

	fun validateAmount(shares: List<RecordShare>) {
		val shouldSum = shares.sumOf { it.shouldAmount }
		val realSum = shares.sumOf { it.realAmount }

		if (shouldSum != realSum) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Amount sum is different")
		}
	}
}
