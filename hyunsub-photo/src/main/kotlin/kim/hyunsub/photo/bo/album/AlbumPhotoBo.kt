package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.api.toApiPreview
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AlbumPhotoBo(
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
) {
	fun list(userId: String, albumId: String, p: Int?, photoId: String?): ApiPageResult<ApiPhotoPreview> {
		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = albumPhotoRepository.countByAlbumId(albumId)

		val page = when {
			photoId != null -> albumPhotoRepository.indexOfPhoto(albumId, photoId) / PhotoConstants.PAGE_SIZE
			else -> p ?: 0
		}

		val pageRequest = PageRequest.of(page, PhotoConstants.PAGE_SIZE)
		val data = albumPhotoRepository.findByAlbumId(albumId, pageRequest).map { it.toApiPreview() }

		return ApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}
}
