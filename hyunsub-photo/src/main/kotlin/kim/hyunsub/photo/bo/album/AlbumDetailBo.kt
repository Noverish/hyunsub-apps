package kim.hyunsub.photo.bo.album

import kim.hyunsub.photo.mapper.AlbumMapper
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.entity.Album
import org.springframework.stereotype.Service

@Service
class AlbumDetailBo(
	private val albumMapper: AlbumMapper,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	fun detail(userId: String, albumId: String): ApiAlbum? {
		val album = albumMapper.selectOne(userId, albumId)
			?: return null
		return toApiAlbum(album)
	}

	fun toApiAlbum(album: Album): ApiAlbum {
		val albumId = album.id
		val total = albumPhotoRepository.countByAlbumId(albumId)

		return ApiAlbum(
			id = album.id,
			name = album.name,
			total = total,
		)
	}
}
