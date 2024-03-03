package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.AlbumPhotoSearchParams
import kim.hyunsub.photo.repository.condition.PhotoCondition2
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumPhotoBo(
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
	private val photoMapper: PhotoMapper,
) {
	fun list(userId: String, albumId: String, params: AlbumPhotoSearchParams): ApiPageResult<ApiPhotoPreview> {
		albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photoId = params.photoId
		val page = when {
			photoId != null -> albumPhotoMapper.indexOfPhoto(albumId, photoId) / PhotoConstants.PAGE_SIZE
			else -> params.page
		}

		val pageRequest = PageRequest.of(page, PhotoConstants.PAGE_SIZE)
		val condition = PhotoCondition2(
			albumId = albumId,
			userIds = params.userIds,
			page = pageRequest,
		)
		val total = photoMapper.countAlbumPhoto(condition)
		val data = photoMapper.selectAlbumPhoto(condition).map { it.toApi() }

		return ApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}
}
