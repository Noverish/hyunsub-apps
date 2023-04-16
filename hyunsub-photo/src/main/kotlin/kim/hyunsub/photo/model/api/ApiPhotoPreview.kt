package kim.hyunsub.photo.model.api

import kim.hyunsub.photo.model.PhotoType
import java.time.OffsetDateTime

data class ApiPhotoPreview(
	val id: String,
	val thumbnail: String,
	val date: OffsetDateTime,
	val type: PhotoType,
	val ext: String,
)
