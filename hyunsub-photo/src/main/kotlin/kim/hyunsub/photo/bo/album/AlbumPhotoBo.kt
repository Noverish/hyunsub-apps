package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApiPreview
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
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
	fun list(userId: String, albumId: String, p: Int?, photoId: String?): ApiPageResult<ApiPhotoPreview> {
		albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = albumPhotoMapper.count(AlbumPhotoCondition(albumId = albumId))

		val page = when {
			photoId != null -> albumPhotoMapper.indexOfPhoto(albumId, photoId) / PhotoConstants.PAGE_SIZE
			else -> p ?: 0
		}

		val pageRequest = PageRequest.of(page, PhotoConstants.PAGE_SIZE)
		val data = photoMapper.selectByAlbumId(albumId, pageRequest).map { it.toApiPreview() }

		return ApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}
}
