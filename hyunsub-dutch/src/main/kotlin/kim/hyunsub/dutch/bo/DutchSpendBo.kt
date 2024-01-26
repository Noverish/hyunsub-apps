package kim.hyunsub.dutch.bo

import kim.hyunsub.common.config.AppConstants
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.dutch.mapper.DutchSpendMapper
import kim.hyunsub.dutch.repository.entity.DutchSpend
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class DutchSpendBo(
	private val dutchSpendMapper: DutchSpendMapper,
) {
	fun list(tripId: String, memberId: String, p: Int?): ApiPageResult<DutchSpend> {
		val page = PageRequest.of(p ?: 0, AppConstants.PAGE_SIZE)
		val total = dutchSpendMapper.count(tripId, memberId)
		val data = dutchSpendMapper.select(tripId, memberId, page)

		return ApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = data,
		)
	}
}
