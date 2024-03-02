package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.photo.model.api.ApiAlbumPreview
import kim.hyunsub.photo.util.PhotoPathConverter
import java.time.LocalDateTime

data class Album(
	val id: String,
	val name: String,
	val thumbnailPhotoId: String? = null,
	val regDt: LocalDateTime = LocalDateTime.now(),
) {
	fun toPreview() = ApiAlbumPreview(
		id = id,
		name = name,
		thumbnail = FsPathConverter.thumbnailUrl(thumbnailPhotoId?.let { PhotoPathConverter.thumbnail(it) }),
	)
}
