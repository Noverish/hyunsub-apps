package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.mapper.AlbumMapper
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumCreateParams
import kim.hyunsub.photo.model.dto.AlbumThumbnailParams
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.generateId
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AlbumMutateBo(
	private val albumRepository: AlbumRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumDetailBo: AlbumDetailBo,
	private val albumMapper: AlbumMapper,
) {
	fun create(userId: String, params: AlbumCreateParams): ApiAlbum {
		val album = Album(
			id = albumRepository.generateId(),
			name = params.name,
		)

		val albumOwner = AlbumOwner(
			albumId = album.id,
			userId = userId,
			owner = true,
		)

		albumRepository.save(album)
		albumOwnerRepository.save(albumOwner)

		return albumDetailBo.toApiAlbum(album)
	}

	fun updateThumbnail(userId: String, albumId: String, params: AlbumThumbnailParams): ApiAlbum {
		val album = albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val photoId = params.photoId
		albumPhotoRepository.findByAlbumIdAndPhotoId(albumId, photoId).firstOrNull()
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such photo")

		val newAlbum = album.copy(thumbnailPhotoId = photoId)
		albumRepository.save(newAlbum)

		return albumDetailBo.toApiAlbum(newAlbum)
	}

	@Transactional
	fun delete(userId: String, albumId: String): ApiAlbum {
		val album = albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")
		val result = albumDetailBo.toApiAlbum(album)

		albumOwnerRepository.deleteByAlbumId(albumId)
		albumPhotoRepository.deleteByAlbumId(albumId)
		albumRepository.deleteById(albumId)

		return result
	}
}
