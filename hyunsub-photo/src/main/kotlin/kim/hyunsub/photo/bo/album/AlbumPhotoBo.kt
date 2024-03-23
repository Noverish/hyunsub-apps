package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.AlbumPhotoCreateParams
import kim.hyunsub.photo.model.dto.AlbumPhotoDeleteParams
import kim.hyunsub.photo.model.dto.AlbumPhotoSearchParams
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.condition.PhotoOfAlbumCondition
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoPreviewMapper
import kim.hyunsub.photo.service.AlbumThumbnailService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumPhotoBo(
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
	private val photoPreviewMapper: PhotoPreviewMapper,
	private val albumThumbnailService: AlbumThumbnailService,
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
		val condition = PhotoOfAlbumCondition(
			albumId = albumId,
			userIds = params.userIds,
			page = pageRequest,
		)
		val total = photoPreviewMapper.countAlbumPhoto(condition)
		val data = photoPreviewMapper.selectAlbumPhoto(condition).map { it.toApi() }

		return ApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}

	fun create(userId: String, albumId: String, params: AlbumPhotoCreateParams): SimpleResponse {
		val photoIds = params.photoIds
		if (photoIds.isEmpty()) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "empty photoIds")
		}

		albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val condition = AlbumPhotoCondition(albumId = albumId, photoIds = photoIds)
		val duplicated = albumPhotoMapper.count(condition)
		if (duplicated > 0) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Already registered photo")
		}

		val albumPhotos = params.photoIds.map {
			AlbumPhoto(
				albumId = albumId,
				photoId = it,
				userId = userId,
			)
		}

		albumPhotoMapper.insertAll(albumPhotos)

		albumThumbnailService.registerRandomIfEmpty(albumId)

		return SimpleResponse()
	}

	fun delete(userId: String, params: AlbumPhotoDeleteParams): SimpleResponse {
		val albumId = params.albumId
		val photoIds = params.photoIds
		if (photoIds.isEmpty()) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "empty photoIds")
		}

		val albumOwner = albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val isOwner = albumOwner.owner

		if (!isOwner) {
			val condition = AlbumPhotoCondition(albumId = albumId, photoIds = photoIds, userId = userId)
			val count = albumPhotoMapper.count(condition)
			if (count != photoIds.size) {
				throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Unauthorized photos")
			}
		}

		albumPhotoMapper.delete(AlbumPhotoCondition(albumId = albumId, photoIds = photoIds))
		albumThumbnailService.unregisterAsync(albumId, photoIds)

		return SimpleResponse()
	}
}
