package kim.hyunsub.photo.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.util.getHumanReadableSize
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoOwner
import kim.hyunsub.photo.util.PhotoPathConverter
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class ApiPhoto(
	val id: String,
	val imageSize: String,
	val fileSize: String,
	val date: OffsetDateTime,
	val fileName: String,
	val regDt: LocalDateTime,
	val dateType: PhotoDateType,
	val original: String,
)

fun Photo.toApi(owner: PhotoOwner) = ApiPhoto(
	id = id,
	imageSize = "$width x $height",
	fileSize = getHumanReadableSize(size.toLong()),
	date = date,
	regDt = owner.regDt,
	fileName = owner.name,
	dateType = dateType,
	original = FsPathConverter.convertToUrl(PhotoPathConverter.original(this))
)
