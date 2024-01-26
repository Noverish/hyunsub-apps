package kim.hyunsub.dutch.bo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.dutch.mapper.DutchRecordMapper
import kim.hyunsub.dutch.mapper.DutchRecordMemberMapper
import kim.hyunsub.dutch.model.api.ApiDutchRecordDetail
import kim.hyunsub.dutch.model.api.ApiDutchRecordPreview
import kim.hyunsub.dutch.model.api.toApi
import kim.hyunsub.dutch.model.dto.DutchRecordSearchParams
import kim.hyunsub.dutch.service.DutchRecordService
import kim.hyunsub.dutch.service.DutchTripDao
import org.springframework.stereotype.Service

@Service
class DutchRecordSearchBo(
	private val dutchTripDao: DutchTripDao,
	private val dutchRecordMapper: DutchRecordMapper,
	private val dutchRecordService: DutchRecordService,
	private val dutchRecordMemberMapper: DutchRecordMemberMapper,
) {
	fun search(params: DutchRecordSearchParams): ApiPageResult<ApiDutchRecordPreview> {
		dutchTripDao.selectOrThrow(params.tripId)

		val total = dutchRecordMapper.searchCount(params)
		val data = dutchRecordMapper.search(params)
			.let { dutchRecordService.convertToApi(it) }

		return ApiPageResult(
			total = total,
			page = params.page ?: 0,
			pageSize = params.pageSize ?: 25,
			data = data,
		)
	}

	fun detail(tripId: String, recordId: String): ApiDutchRecordDetail? {
		val preview = dutchRecordMapper.select(tripId, recordId)
			?.let { dutchRecordService.convertToApi(it) }
			?: return null

		val members = dutchRecordMemberMapper.selectByRecordId(recordId)
			.map { it.toApi() }

		return ApiDutchRecordDetail(preview, members)
	}
}
