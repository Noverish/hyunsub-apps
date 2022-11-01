package kim.hyunsub.division.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.division.model.ReportResult
import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.repository.GatheringUserRepository
import kim.hyunsub.division.repository.RecordShareRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class ReportService(
	private val recordShareRepository: RecordShareRepository,
	private val recordService: RecordService,
	private val gatheringUserRepository: GatheringUserRepository,
) {
	val log = KotlinLogging.logger { }

	fun reportAll(gatheringId: String, leaderUserId: String): List<ReportResult> {
		val users = gatheringUserRepository.findByGatheringId(gatheringId)
		log.debug { "[Report] users=$users" }

		if (users.none { it.userId == leaderUserId }) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No such leader user id")
		}

		val records = recordService.list(gatheringId)
		return records.groupBy { it.currency }
			.map { (currency, list) ->
				ReportResult(
					currency = currency,
					data = reportWithCurrency(list, leaderUserId)
				)
			}
	}

	private fun reportWithCurrency(list: List<RestApiRecord>, leaderUserId: String): Map<String, Int> {
		val map = mutableMapOf<String, Int>()

		for (item in list) {
			for (share in item.shares) {
				val isLeader = share.userId == leaderUserId
				if (isLeader) {
					continue
				}

				val payToLeader = share.shouldAmount - share.realAmount
				map[share.userId] = (map[share.userId] ?: 0) + payToLeader
			}
		}

		return map
	}
}
