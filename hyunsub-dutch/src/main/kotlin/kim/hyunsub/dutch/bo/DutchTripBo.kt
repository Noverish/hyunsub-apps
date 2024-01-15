package kim.hyunsub.dutch.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.mapper.DutchRecordMapper
import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.mapper.DutchTripMapper
import kim.hyunsub.dutch.mapper.generateId
import kim.hyunsub.dutch.model.api.ApiDutchTrip
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchTripCreateParams
import kim.hyunsub.dutch.repository.entity.DutchMember
import kim.hyunsub.dutch.repository.entity.DutchTrip
import org.springframework.stereotype.Service

@Service
class DutchTripBo(
	private val dutchTripMapper: DutchTripMapper,
	private val dutchMemberMapper: DutchMemberMapper,
	private val dutchRecordMapper: DutchRecordMapper,
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
) {
	fun detail(tripId: String): ApiDutchTrip? {
		return dutchTripMapper.select(tripId)?.toApi()
	}

	fun create(params: DutchTripCreateParams): ApiDutchTrip {
		val trip = DutchTrip(
			id = dutchTripMapper.generateId(),
			name = params.name,
			currency = params.currency,
		)

		val members = params.members.map {
			DutchMember(
				id = dutchMemberMapper.generateId(),
				name = it,
				tripId = trip.id,
			)
		}

		dutchTripMapper.insert(trip)
		dutchMemberMapper.insertAll(members)

		return trip.toApi()
	}

	fun delete(tripId: String): ApiDutchTrip {
		val trip = dutchTripMapper.select(tripId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such trip: $tripId")

		dutchRecordMemberMapper.deleteByTripId(tripId)
		dutchMemberMapper.deleteByTripId(tripId)
		dutchRecordMapper.deleteByTripId(tripId)
		dutchTripMapper.delete(tripId)

		return trip.toApi()
	}
}
