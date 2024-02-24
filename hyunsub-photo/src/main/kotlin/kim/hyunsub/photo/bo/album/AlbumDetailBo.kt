package kim.hyunsub.photo.bo.album

import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import org.springframework.stereotype.Service

@Service
class AlbumDetailBo(
	private val albumMapper: AlbumMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
) {
	fun detail(userId: String, albumId: String): ApiAlbum? {
		val album = albumMapper.selectOne(id = albumId, userId = userId)
			?: return null
		return toApiAlbum(album)
	}

	fun toApiAlbum(album: Album): ApiAlbum {
		val albumId = album.id
		val total = albumPhotoMapper.count(AlbumPhotoCondition(albumId = albumId))

		return ApiAlbum(
			id = album.id,
			name = album.name,
			total = total,
		)
	}
}
