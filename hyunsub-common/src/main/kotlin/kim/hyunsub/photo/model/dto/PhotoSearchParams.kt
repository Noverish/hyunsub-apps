package kim.hyunsub.photo.model.dto

import kim.hyunsub.common.model.LocalDateRange
import java.time.LocalDate

data class PhotoSearchParams(
	val date: LocalDate? = null,
	val dateRange: LocalDateRange? = null,
	val myPhotoOnly: Boolean? = null,
	val page: Int? = null,
	val pageSize: Int? = null,
	val photoId: String? = null,
)
