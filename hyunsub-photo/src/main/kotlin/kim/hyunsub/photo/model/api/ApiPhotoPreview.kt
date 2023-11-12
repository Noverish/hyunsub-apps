package kim.hyunsub.photo.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.photo.model.PhotoType
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.util.PhotoPathConverter

fun Photo.toApiPreview() = ApiPhotoPreview(
	id = id,
	thumbnail = FsPathConverter.convertToUrl(PhotoPathConverter.thumbnail(id)),
	date = date,
	type = if (isVideo) PhotoType.VIDEO else PhotoType.PHOTO,
	ext = ext,
)
