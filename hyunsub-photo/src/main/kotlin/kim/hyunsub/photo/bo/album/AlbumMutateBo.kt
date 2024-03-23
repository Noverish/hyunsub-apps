package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumCreateParams
import kim.hyunsub.photo.model.dto.AlbumThumbnailParams
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import kim.hyunsub.photo.repository.mapper.generateId
import org.springframework.stereotype.Service

@Service
class AlbumMutateBo(
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val albumDetailBo: AlbumDetailBo,
	private val albumMapper: AlbumMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	fun create(userId: String, params: AlbumCreateParams): ApiAlbum {
		val album = Album(
			id = albumMapper.generateId(),
			name = params.name,
		)

		val albumOwner = AlbumOwner(
			albumId = album.id,
			userId = userId,
			owner = true,
		)

		albumMapper.insert(album)
		albumOwnerMapper.insert(albumOwner)

		return albumDetailBo.toApiAlbum(album)
	}

	fun updateThumbnail(userId: String, albumId: String, params: AlbumThumbnailParams): SimpleResponse {
		val albumOwner = albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		if (!albumOwner.owner) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No authority to this album")
		}

		val photoId = params.photoId
		albumPhotoMapper.selectOne(albumId = albumId, photoId = photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		val album = albumMapper.selectOne(albumId) ?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")
		val newAlbum = album.copy(thumbnailPhotoId = photoId)
		albumMapper.update(newAlbum)

		return SimpleResponse()
	}

	fun delete(userId: String, albumId: String): SimpleResponse {
		val albumOwner = albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		if (!albumOwner.owner) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No authority to this album")
		}

		albumOwnerMapper.deleteByAlbumId(albumId)
		albumPhotoMapper.delete(AlbumPhotoCondition(albumId = albumId))
		albumMapper.deleteById(albumId)

		return SimpleResponse()
	}
}
