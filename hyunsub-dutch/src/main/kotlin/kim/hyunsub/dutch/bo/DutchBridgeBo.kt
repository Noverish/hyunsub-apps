package kim.hyunsub.dutch.bo

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.mapper.DutchTripMapper
import kim.hyunsub.dutch.model.DutchMemberAuth
import kim.hyunsub.dutch.service.DutchUserAuthService
import org.springframework.stereotype.Service

@Service
class DutchBridgeBo(
	private val dutchTripMapper: DutchTripMapper,
	private val dutchMemberMapper: DutchMemberMapper,
	private val dutchUserAuthService: DutchUserAuthService,
) {
	fun entry(dutchMemberAuth: DutchMemberAuth?, tripId: String): String {
		dutchTripMapper.select(tripId)
			?: return "/404"

		val memberId = dutchMemberAuth?.memberId
			?: return "/trips/$tripId/member-select"

		dutchMemberMapper.select(memberId, tripId)
			?: return "/trips/$tripId/member-select"

		return "/trips/$tripId"
	}

	fun memberSelect(req: HttpServletRequest, res: HttpServletResponse, tripId: String, memberId: String): String {
		val auth = dutchUserAuthService.parse(req, tripId)
		val newAuth = auth?.copy(memberId = memberId)
			?: DutchMemberAuth(memberId)
		dutchUserAuthService.save(req, res, tripId, newAuth)
		return "/trips/$tripId"
	}
}
