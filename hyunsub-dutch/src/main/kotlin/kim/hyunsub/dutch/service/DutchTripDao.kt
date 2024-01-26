package kim.hyunsub.dutch.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchTripMapper
import kim.hyunsub.dutch.repository.entity.DutchTrip
import org.springframework.stereotype.Service

@Service
class DutchTripDao(
	private val dutchTripMapper: DutchTripMapper,
) {
	fun selectOrThrow(tripId: String): DutchTrip =
		dutchTripMapper.select(tripId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such trip: $tripId")
}
