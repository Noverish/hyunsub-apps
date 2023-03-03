package kim.hyunsub.photo.service

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.util.getHumanReadableSize
import kim.hyunsub.photo.model.RestApiAlbum
import kim.hyunsub.photo.model.RestApiAlbumPreview
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.entity.Photo
import org.springframework.stereotype.Service

@Service
class ApiModelConverter(
	private val fileUrlConverter: FileUrlConverter,
	private val thumbnailService: ThumbnailService,
) {
	fun convertToPreview(album: Album) =
		RestApiAlbumPreview(
			id = album.id,
			name = album.name,
			thumbnail = thumbnailService.getThumbnailUrl(album.thumbnail),
		)

	fun convert(album: Album, total: Int) =
		RestApiAlbum(
			id = album.id,
			name = album.name,
			thumbnail = thumbnailService.getThumbnailUrl(album.thumbnail),
			photos = total,
		)

	fun convert(photo: Photo) =
		RestApiPhoto(
			id = photo.id,
			thumbnail = thumbnailService.getThumbnailUrl(photo.path),
			url = fileUrlConverter.pathToUrl(photo.path),
			width = photo.width,
			height = photo.height,
			date = photo.date,
			size = getHumanReadableSize(photo.size.toLong()),
		)
}
