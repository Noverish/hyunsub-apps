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
		val photoId = params.photoId
		if (photoId != null) {
			return searchWithPhotoId(userId, photoId, params)
		}

		return searchWithPage(userId, params.page ?: 0, params)
	}

	private fun searchWithPhotoId(userId: String, photoId: String, params: PhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		val pageReq = PageRequest.of(0, params.pageSize ?: PhotoConstants.PAGE_SIZE)
		val condition = PhotoPreviewCondition(
			userId = userId,
			dateRange = params.dateRange?.toLdtRange(),
			page = pageReq,
			photoId = photoId,
		)

		val count = photoPreviewMapper.count(condition)
		val page = count / pageReq.pageSize

		return searchWithPage(userId, page, params)
	}

	private fun searchWithPage(userId: String, page: Int, params: PhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		val pageReq = PageRequest.of(page, params.pageSize ?: PhotoConstants.PAGE_SIZE)
		val condition = PhotoPreviewCondition(
			userId = userId,
			dateRange = params.dateRange?.toLdtRange(),
			page = pageReq,
			orphan = params.orphan,
		)

		val total = photoPreviewMapper.count(condition)
		val result = photoPreviewMapper.select(condition)

		return ApiPageResult(
			total = total,
			page = pageReq.pageNumber,
			pageSize = pageReq.pageSize,
			data = result.map { it.toApi() }
		)
	}
}
