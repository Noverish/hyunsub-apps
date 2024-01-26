package kim.hyunsub.dutch.bo

import kim.hyunsub.common.config.AppConstants
import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.dutch.mapper.DutchSpendMapper
import kim.hyunsub.dutch.model.dto.DutchSpendSearchQuery
import kim.hyunsub.dutch.repository.entity.DutchSpend
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class DutchSpendBo(
	private val dutchSpendMapper: DutchSpendMapper,
) {
	fun list(memberId: String, p: Int?): ApiPageResult<DutchSpend> {
		val page = PageRequest.of(p ?: 0, AppConstants.PAGE_SIZE)
		val query = DutchSpendSearchQuery(memberId = memberId, page = page)
		val total = dutchSpendMapper.count(query)
		val data = dutchSpendMapper.search(query)

		return ApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = data,
		)
	}
}
