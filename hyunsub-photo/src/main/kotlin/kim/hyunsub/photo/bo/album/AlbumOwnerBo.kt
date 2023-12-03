package kim.hyunsub.photo.bo.album

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.mapper.AlbumMapper
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumOwnerParams
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AlbumOwnerBo(
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumDetailBo: AlbumDetailBo,
	private val albumMapper: AlbumMapper,
) {
	fun create(userId: String, albumId: String, params: AlbumOwnerParams): ApiAlbum {
		val album = albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val albumOwner = AlbumOwner(albumId, params.userId, false)
		albumOwnerRepository.save(albumOwner)

		return albumDetailBo.toApiAlbum(album)
	}

	fun delete(userId: String, albumId: String, params: AlbumOwnerParams): ApiAlbum {
		val album = albumMapper.selectOne(userId, albumId, owner = true)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")

		val albumOwner = albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, params.userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album owner")
		albumOwnerRepository.delete(albumOwner)

		return albumDetailBo.toApiAlbum(album)
	}
}
