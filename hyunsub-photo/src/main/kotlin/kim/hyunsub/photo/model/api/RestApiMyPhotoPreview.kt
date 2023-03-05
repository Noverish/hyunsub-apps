package kim.hyunsub.photo.model.api

import java.time.OffsetDateTime

data class RestApiMyPhotoPreview(
	val id: String,
	val thumbnail: String,
	val date: OffsetDateTime,
)
