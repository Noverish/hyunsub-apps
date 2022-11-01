package kim.hyunsub.division.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.division.repository.GatheringRepository
import kim.hyunsub.division.repository.GatheringUserRepository
import kim.hyunsub.division.repository.entity.Gathering
import kim.hyunsub.division.repository.entity.GatheringUserId
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GatheringService(
	private val gatheringRepository: GatheringRepository,
	private val gatheringUserRepository: GatheringUserRepository,
) {
	val log = KotlinLogging.logger { }

	fun detail(gatheringId: String, userId: String? = null): Gathering {
		log.debug { "[Gathering Detail] gatheringId=$gatheringId, userId=$userId" }

		val gathering = gatheringRepository.findByIdOrNull(gatheringId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Gathering Detail] gathering=$gathering" }

		if (userId != null) {
			val gatheringUser = gatheringUserRepository.findById(GatheringUserId(gatheringId, userId))
				?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
			log.debug { "[Gathering Detail] gatheringUser=$gatheringUser" }
		}

		return gathering
	}
}
