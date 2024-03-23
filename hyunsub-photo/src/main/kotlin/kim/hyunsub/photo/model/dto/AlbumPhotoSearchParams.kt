package kim.hyunsub.photo.model.dto

import kim.hyunsub.common.model.LocalDateRange

data class AlbumPhotoSearchParams(
	val dateRange: LocalDateRange? = null,
	val page: Int = 0,
	val photoId: String? = null,
	val userIds: List<String>? = null,
)
