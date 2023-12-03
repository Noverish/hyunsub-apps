package kim.hyunsub.photo.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.repository.entity.PhotoOwnerId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PhotoBo(
	private val photoRepository: PhotoRepository,
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	fun detail(userId: String, photoId: String, albumId: String?): ApiPhoto {
		return albumId?.let { detailWithAlbum(userId, photoId, it) }
			?: detailWithoutAlbum(userId, photoId)
	}

	private fun detailWithAlbum(userId: String, photoId: String, albumId: String): ApiPhoto {
		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album owner: userId=$userId, albumId=$albumId")

		val albumPhoto = albumPhotoRepository.findByIdOrNull(AlbumPhotoId(albumId, photoId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album photo: photoId=$photoId, albumId=$albumId")

		return detailWithoutAlbum(albumPhoto.userId, photoId)
	}

	private fun detailWithoutAlbum(userId: String, photoId: String): ApiPhoto {
		val photoOwner = photoOwnerRepository.findByIdOrNull(PhotoOwnerId(userId, photoId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo owner: userId=$userId, photoId=$photoId")

		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo: photoId=$photoId")

		return photo.toApi(photoOwner)
	}
}
