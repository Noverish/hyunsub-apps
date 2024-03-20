package kim.hyunsub.photo.model.api

import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.model.PhotoType
import java.time.OffsetDateTime

data class ApiPhotoPreview(
	val id: String,
	val thumbnail: String,
	val date: OffsetDateTime,
	val type: PhotoType,
	val ext: String,
	val userId: String,
	val fileName: String,
	val imageSize: String,
	val fileSize: String,
	val dateType: PhotoDateType,
)
