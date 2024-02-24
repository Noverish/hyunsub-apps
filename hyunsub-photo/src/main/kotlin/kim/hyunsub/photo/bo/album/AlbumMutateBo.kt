package kim.hyunsub.photo.bo.album

import jakarta.transaction.Transactional
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumCreateParams
import kim.hyunsub.photo.model.dto.AlbumThumbnailParams
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

	fun updateThumbnail(userId: String, albumId: String, params: AlbumThumbnailParams): ApiAlbum {
		val album = albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val photoId = params.photoId
		albumPhotoMapper.selectOne(albumId = albumId, photoId = photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		val newAlbum = album.copy(thumbnailPhotoId = photoId)
		albumMapper.insert(newAlbum)

		return albumDetailBo.toApiAlbum(newAlbum)
	}

	@Transactional
	fun delete(userId: String, albumId: String): ApiAlbum {
		val album = albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")
		val result = albumDetailBo.toApiAlbum(album)

		albumOwnerMapper.deleteByAlbumId(albumId)
		albumPhotoMapper.deleteByAlbumId(albumId)
		albumMapper.deleteById(albumId)

		return result
	}
}
