package kim.hyunsub.photo.bo.photo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.toApi
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.PhotoOwnerMapper
import org.springframework.stereotype.Service

@Service
class PhotoDetailBo(
	private val photoMapper: PhotoMapper,
	private val photoOwnerMapper: PhotoOwnerMapper,
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	fun detail(userId: String, photoId: String, albumId: String?): ApiPhoto {
		return albumId?.let { detailWithAlbum(userId, photoId, it) }
			?: detailWithoutAlbum(userId, photoId)
	}

	private fun detailWithAlbum(userId: String, photoId: String, albumId: String): ApiPhoto {
		albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album owner")

		val albumPhoto = albumPhotoMapper.selectOne(albumId = albumId, photoId = photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album photo")

		return detailWithoutAlbum(albumPhoto.userId, photoId)
	}

	private fun detailWithoutAlbum(userId: String, photoId: String): ApiPhoto {
		val photoOwner = photoOwnerMapper.selectOne(userId = userId, photoId = photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo owner")

		val photo = photoMapper.selectOne(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		return photo.toApi(photoOwner)
	}
}
