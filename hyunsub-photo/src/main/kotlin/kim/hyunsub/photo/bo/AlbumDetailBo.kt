package kim.hyunsub.photo.bo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.api.toApiPreview
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.entity.Album
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AlbumDetailBo(
	private val albumRepository: AlbumRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	fun detail(userId: String, albumId: String): ApiAlbum? {
		val album = albumRepository.findByAlbumIdAndUserId(albumId, userId)
			?: return null
		return toApiAlbum(album)
	}

	fun toApiAlbum(album: Album): ApiAlbum {
		val albumId = album.id
		val total = albumPhotoRepository.countByAlbumId(albumId)

		val page = PageRequest.ofSize(PhotoConstants.PAGE_SIZE)
		val photos = albumPhotoRepository.findByAlbumId(albumId, page).map { it.toApiPreview() }

		return ApiAlbum(
			id = album.id,
			name = album.name,
			photos = ApiPageResult(
				total = total,
				page = 0,
				pageSize = PhotoConstants.PAGE_SIZE,
				data = photos,
			),
		)
	}
}
