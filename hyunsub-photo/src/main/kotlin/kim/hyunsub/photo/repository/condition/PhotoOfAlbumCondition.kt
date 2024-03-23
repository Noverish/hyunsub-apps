package kim.hyunsub.photo.repository.condition

import kim.hyunsub.common.model.LocalDateTimeRange
import org.springframework.data.domain.Pageable

data class PhotoOfAlbumCondition(
	val albumId: String,
	val dateRange: LocalDateTimeRange? = null,
	val userIds: List<String>? = null,
	val photoId: String? = null,
	val page: Pageable? = null,
)
