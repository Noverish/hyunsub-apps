package kim.hyunsub.photo.model.dto

import kim.hyunsub.common.model.LocalDateRange

data class PhotoSearchParams(
	val dateRange: LocalDateRange? = null,
	val orphan: Boolean? = null,
	val page: Int = 0,
	val photoId: String? = null,
)
