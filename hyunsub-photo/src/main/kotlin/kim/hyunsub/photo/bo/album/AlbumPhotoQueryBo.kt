package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.AlbumPhotoSearchParams
import kim.hyunsub.photo.repository.condition.PhotoOfAlbumCondition
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.PhotoPreviewMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumPhotoQueryBo(
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val photoPreviewMapper: PhotoPreviewMapper,
) {
	fun search(userId: String, albumId: String, params: AlbumPhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val page = decidePage(albumId, params)

		val pageReq = PageRequest.of(page, PhotoConstants.PAGE_SIZE)
		val condition = PhotoOfAlbumCondition(
			albumId = albumId,
			dateRange = params.dateRange?.toLdtRange(),
			userIds = params.userIds,
			page = pageReq,
		)
		val total = photoPreviewMapper.countAlbumPhoto(condition)
		val data = photoPreviewMapper.selectAlbumPhoto(condition)

		return ApiPageResult(
			total = total,
			page = pageReq.pageNumber,
			pageSize = pageReq.pageSize,
			data = data.map { it.toApi() },
		)
	}

	private fun decidePage(albumId: String, params: AlbumPhotoSearchParams): Int {
		val photoId = params.photoId ?: return params.page

		val pageReq = PageRequest.of(0, PhotoConstants.PAGE_SIZE)
		val condition = PhotoOfAlbumCondition(
			albumId = albumId,
			dateRange = params.dateRange?.toLdtRange(),
			userIds = params.userIds,
			page = pageReq,
			photoId = photoId,
		)

		val count = photoPreviewMapper.countAlbumPhoto(condition)
		return count / pageReq.pageSize
	}
}
