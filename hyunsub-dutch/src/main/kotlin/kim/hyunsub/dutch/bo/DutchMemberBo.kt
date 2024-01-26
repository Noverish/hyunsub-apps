package kim.hyunsub.dutch.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.mapper.generateId
import kim.hyunsub.dutch.model.api.ApiDutchMember
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchMemberCreateParams
import kim.hyunsub.dutch.repository.entity.DutchMember
import kim.hyunsub.dutch.service.DutchTripDao
import org.springframework.stereotype.Service

@Service
class DutchMemberBo(
	private val dutchTripDao: DutchTripDao,
	private val dutchMemberMapper: DutchMemberMapper,
) {
	fun list(tripId: String): List<ApiDutchMember> {
		return dutchMemberMapper.selectByTripId(tripId).map { it.toApi() }
	}

	fun create(tripId: String, params: DutchMemberCreateParams): ApiDutchMember {
		dutchTripDao.selectOrThrow(tripId)

		val member = DutchMember(
			id = dutchMemberMapper.generateId(),
			name = params.name,
			tripId = tripId
		)

		dutchMemberMapper.insertAll(listOf(member))

		return member.toApi()
	}

	fun delete(tripId: String, memberId: String): ApiDutchMember {
		dutchTripDao.selectOrThrow(tripId)

		val member = dutchMemberMapper.select(memberId, tripId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such member")

		dutchMemberMapper.delete(memberId, tripId)

		return member.toApi()
	}
}
