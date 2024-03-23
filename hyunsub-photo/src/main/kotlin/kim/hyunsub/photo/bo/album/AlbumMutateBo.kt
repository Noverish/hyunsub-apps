package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumCreateParams
import kim.hyunsub.photo.model.dto.AlbumUpdateParams
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
	private val albumQueryBo: AlbumQueryBo,
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

		return albumQueryBo.toApiAlbum(album)
	}

	fun update(userId: String, albumId: String, params: AlbumUpdateParams): SimpleResponse {
		val album = albumMapper.selectOne(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val albumOwner = albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		if (!albumOwner.owner) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No authority to this album")
		}

		val newAlbum = album
			.let {
				val thumbnailId = params.thumbnailId ?: return@let it
				albumPhotoMapper.selectOne(albumId = albumId, photoId = thumbnailId)
					?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")
				it.copy(thumbnailPhotoId = thumbnailId)
			}
			.let {
				val name = params.name ?: return@let it
				it.copy(name = name)
			}

		if (album == newAlbum) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Nothing has changed")
		}

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
