package kim.hyunsub.dutch.bo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.dutch.mapper.DutchRecordMapper
import kim.hyunsub.dutch.model.api.ApiDutchRecord
import kim.hyunsub.dutch.model.dto.DutchSearchParams
import kim.hyunsub.dutch.service.DutchRecordService
import org.springframework.stereotype.Service

@Service
class DutchRecordSearchBo(
	private val dutchRecordMapper: DutchRecordMapper,
	private val dutchRecordService: DutchRecordService,
) {
	fun search(params: DutchSearchParams): ApiPageResult<ApiDutchRecord> {
		val total = dutchRecordMapper.count(params)
		val data = dutchRecordMapper.search(params)
			.let { dutchRecordService.convertToApi(it) }

		return ApiPageResult(
			total = total,
			page = params.page ?: 0,
			pageSize = params.pageSize ?: 25,
			data = data,
		)
	}

	fun detail(tripId: String, recordId: String): ApiDutchRecord? {
		return dutchRecordMapper.selectOne(tripId, recordId)
			?.let { dutchRecordService.convertToApi(it) }
	}
}
