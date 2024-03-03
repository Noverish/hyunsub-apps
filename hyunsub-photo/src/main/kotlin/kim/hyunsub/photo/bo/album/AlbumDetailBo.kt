package kim.hyunsub.photo.bo.album

import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.api.ApiAlbumMember
import kim.hyunsub.photo.repository.condition.AlbumOwnerCondition
import kim.hyunsub.photo.repository.condition.AlbumPhotoCondition
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.AlbumPhotoMapper
import org.springframework.stereotype.Service

@Service
class AlbumDetailBo(
	private val albumMapper: AlbumMapper,
	private val albumPhotoMapper: AlbumPhotoMapper,
	private val albumOwnerMapper: AlbumOwnerMapper,
) {
	fun detail(userId: String, albumId: String): ApiAlbum? {
		val album = albumMapper.selectWithUserId(albumId = albumId, userId = userId)
			?: return null

		return toApiAlbum(album)
	}

	fun toApiAlbum(album: Album): ApiAlbum {
		val albumId = album.id
		val total = albumPhotoMapper.count(AlbumPhotoCondition(albumId = albumId))

		val albumOwners = albumOwnerMapper.select2(AlbumOwnerCondition(albumId = albumId))
		val members = albumOwners.map { ApiAlbumMember(it.userId, it.username) }

		return ApiAlbum(
			id = album.id,
			name = album.name,
			total = total,
			members = members,
		)
	}
}
