package kim.hyunsub.photo.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
class AlbumDeleteBo(
	private val albumRepository: AlbumRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumDetailBo: AlbumDetailBo,
) {
	@Transactional
	fun delete(userId: String, albumId: String): ApiAlbum {
		val album = albumRepository.findByAlbumIdAndUserId(albumId, userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such album")
		val result = albumDetailBo.toApiAlbum(album)

		albumOwnerRepository.deleteByAlbumId(albumId)
		albumPhotoRepository.deleteByAlbumId(albumId)
		albumRepository.deleteById(albumId)

		return result
	}
}
