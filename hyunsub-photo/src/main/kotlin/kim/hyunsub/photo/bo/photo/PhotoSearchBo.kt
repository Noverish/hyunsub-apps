package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import kim.hyunsub.photo.repository.condition.PhotoCondition
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PhotoSearchBo(
	private val photoMapper: PhotoMapper,
) {
	fun search(userId: String, params: PhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		val page = PageRequest.of(params.page ?: 0, params.pageSize ?: PhotoConstants.PAGE_SIZE)
		val condition = PhotoCondition(
			userId = userId,
			dateRange = params.dateRange,
			page = page,
		)

		val total = photoMapper.count(condition)
		val result = photoMapper.select(condition)

		return ApiPageResult(
			total = total,
			page = page.pageNumber,
			pageSize = page.pageSize,
			data = result.map { it.toApi() }
		)
	}
}
