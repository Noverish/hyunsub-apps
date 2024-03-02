package kim.hyunsub.photo.repository.entity

import kim.hyunsub.common.util.toOdt
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.util.isVideo
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

data class PhotoPreview(
	val id: String,
	val width: Int,
	val height: Int,
	val size: Int,
	val ext: String,
	val pairPhotoId: String? = null,
	val date: LocalDateTime,
	val offset: Int,
	val userId: String,
	val name: String,
	val dateType: PhotoDateType,
	val idNew: String,
) {
	val odt: OffsetDateTime
		get() = date.toOdt(ZoneOffset.ofTotalSeconds(offset))

	private val fileName: String
		get() = "$id.$ext"

	val isVideo: Boolean
		get() = isVideo(fileName)

	constructor(photo: Photo, owner: PhotoOwner) : this(
		id = photo.id,
		width = photo.width,
		height = photo.height,
		size = photo.size,
		ext = photo.ext,
		pairPhotoId = photo.pairPhotoId,
		date = owner.date,
		offset = owner.offset,
		userId = owner.userId,
		name = owner.name,
		dateType = owner.dateType,
		idNew = photo.idNew
	)
}
