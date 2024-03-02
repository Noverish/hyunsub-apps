package kim.hyunsub.photo.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.photo.model.PhotoType
import kim.hyunsub.photo.repository.entity.PhotoPreview
import kim.hyunsub.photo.util.PhotoPathConverter

fun PhotoPreview.toApi() = ApiPhotoPreview(
	id = id,
	thumbnail = FsPathConverter.convertToUrl(PhotoPathConverter.thumbnailNew(this)),
	date = odt,
	type = if (isVideo) PhotoType.VIDEO else PhotoType.PHOTO,
	ext = ext,
)
