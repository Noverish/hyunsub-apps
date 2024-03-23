package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.model.dto.AlbumPhotoDeleteBulkParams
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoPreviewMapper
import kim.hyunsub.photo.service.AlbumThumbnailService
import org.springframework.stereotype.Service

@Service
class AlbumPhotoDeleteBo(
	private val albumMapper: AlbumMapper,
	private val albumThumbnailService: AlbumThumbnailService,
	private val photoPreviewMapper: PhotoPreviewMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	fun deleteBulk(userId: String, params: AlbumPhotoDeleteBulkParams): List<ApiPhotoPreview> {
		return params.photoIds.map { delete(userId, params.albumId, it) }
	}

	fun delete(userId: String, albumId: String, photoId: String): ApiPhotoPreview {
		albumMapper.selectWithUserId(userId = userId, albumId = albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val albumPhoto = albumPhotoMapper.selectOne(albumId = albumId, photoId = photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album photo")

		val photo = photoPreviewMapper.selectOne(id = photoId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		albumPhotoMapper.delete(albumPhoto)

		albumThumbnailService.deleteAsync(photoId)

		return photo.toApi()
	}
}
