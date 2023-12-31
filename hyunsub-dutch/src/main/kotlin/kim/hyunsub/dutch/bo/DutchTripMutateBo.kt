package kim.hyunsub.dutch.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.mapper.DutchRecordMapper
import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchTrip
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchTripCreateParams
import kim.hyunsub.dutch.repository.DutchMemberRepository
import kim.hyunsub.dutch.repository.DutchTripRepository
import kim.hyunsub.dutch.repository.entity.DutchMember
import kim.hyunsub.dutch.repository.entity.DutchTrip
import kim.hyunsub.dutch.repository.generateId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DutchTripMutateBo(
	private val dutchTripRepository: DutchTripRepository,
	private val dutchMemberRepository: DutchMemberRepository,
	private val dutchMemberMapper: DutchMemberMapper,
	private val dutchRecordMapper: DutchRecordMapper,
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
) {
	fun create(params: DutchTripCreateParams): ApiDutchTrip {
		val trip = DutchTrip(
			id = dutchTripRepository.generateId(),
			name = params.name,
			currency = params.currency,
		)

		val members = params.members.map {
			DutchMember(
				id = dutchMemberRepository.generateId(),
				name = it,
				tripId = trip.id,
			)
		}

		dutchTripRepository.save(trip)
		dutchMemberRepository.saveAll(members)

		return trip.toApi()
	}

	fun delete(tripId: String): ApiDutchTrip {
		val trip = dutchTripRepository.findByIdOrNull(tripId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such trip: $tripId")

		dutchRecordMemberMapper.deleteByTripId(tripId)
		dutchMemberMapper.deleteByTripId(tripId)
		dutchRecordMapper.deleteByTripId(tripId)
		dutchTripRepository.delete(trip)

		return trip.toApi()
	}
}
