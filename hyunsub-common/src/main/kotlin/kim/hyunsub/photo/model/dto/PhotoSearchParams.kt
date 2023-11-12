package kim.hyunsub.photo.model.dto

import java.time.LocalDate

data class PhotoSearchParams(
	val date: LocalDate,
	val myPhotoOnly: Boolean,
	val page: Int?,
	val pageSize: Int?,
)
