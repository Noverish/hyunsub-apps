package kim.hyunsub.photo.model.dto

import kim.hyunsub.common.model.LocalDateRange

data class PhotoSearchParams(
	val dateRange: LocalDateRange? = null,
	val myPhotoOnly: Boolean? = null,
	val page: Int? = null,
	val pageSize: Int? = null,
	val photoId: String? = null,
)
