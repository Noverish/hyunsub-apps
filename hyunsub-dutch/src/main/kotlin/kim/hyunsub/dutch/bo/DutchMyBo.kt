package kim.hyunsub.dutch.bo

import kim.hyunsub.dutch.mapper.DutchMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchMember
import kim.hyunsub.dutch.model.api.toApi
import org.springframework.stereotype.Service

@Service
class DutchMyBo(
	private val dutchMemberMapper: DutchMemberMapper,
) {
	fun my(tripId: String, memberId: String): ApiDutchMember? {
		return dutchMemberMapper.select(memberId, tripId)
			?.toApi()
	}
}
