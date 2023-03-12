package kim.hyunsub.photo.model.api

import java.time.OffsetDateTime

data class RestApiPhotoPreview(
	val id: String,
	val thumbnail: String,
	val date: OffsetDateTime,
)
