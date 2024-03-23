package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import kim.hyunsub.photo.repository.condition.PhotoPreviewCondition
import kim.hyunsub.photo.repository.mapper.PhotoPreviewMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class PhotoSearchBo(
	private val photoPreviewMapper: PhotoPreviewMapper,
) {
	fun search(userId: String, params: PhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		val page = decidePage(userId, params)

		val pageReq = PageRequest.of(page, PhotoConstants.PAGE_SIZE)
		val condition = PhotoPreviewCondition(
			userId = userId,
			dateRange = params.dateRange?.toLdtRange(),
			orphan = params.orphan,
			page = pageReq,
		)

		val total = photoPreviewMapper.count(condition)
		val data = photoPreviewMapper.select(condition)

		return ApiPageResult(
			total = total,
			page = pageReq.pageNumber,
			pageSize = pageReq.pageSize,
			data = data.map { it.toApi() }
		)
	}

	private fun decidePage(userId: String, params: PhotoSearchParams): Int {
		val photoId = params.photoId ?: return params.page

		val pageReq = PageRequest.of(0, PhotoConstants.PAGE_SIZE)
		val condition = PhotoPreviewCondition(
			userId = userId,
			dateRange = params.dateRange?.toLdtRange(),
			orphan = params.orphan,
			page = pageReq,
			photoId = photoId,
		)

		val count = photoPreviewMapper.count(condition)
		return count / pageReq.pageSize
	}
}
