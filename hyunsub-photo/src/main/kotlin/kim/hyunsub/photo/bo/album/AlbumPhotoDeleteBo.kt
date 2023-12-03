package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.mapper.AlbumMapper
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApiPreview
import kim.hyunsub.photo.model.dto.AlbumPhotoDeleteBulkParams
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.service.AlbumThumbnailService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AlbumPhotoDeleteBo(
	private val photoRepository: PhotoRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumMapper: AlbumMapper,
	private val albumThumbnailService: AlbumThumbnailService,
) {
	fun deleteBulk(userId: String, params: AlbumPhotoDeleteBulkParams): List<ApiPhotoPreview> {
		return params.photoIds.map { delete(userId, params.albumId, it) }
	}

	fun delete(userId: String, albumId: String, photoId: String): ApiPhotoPreview {
		albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val albumPhoto = albumPhotoRepository.findByIdOrNull(AlbumPhotoId(albumId, photoId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album photo")

		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		albumPhotoRepository.delete(albumPhoto)

		albumThumbnailService.delete(photoId)

		return photo.toApiPreview()
	}
}
