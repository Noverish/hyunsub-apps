package kim.hyunsub.photo.repository.condition

import kim.hyunsub.common.model.LocalDateTimeRange
import org.springframework.data.domain.Pageable

data class PhotoCondition(
	val userId: String,
	val dateRange: LocalDateTimeRange? = null,
	val page: Pageable,
	val photoId: String? = null,
	val orphan: Boolean? = null,
)
