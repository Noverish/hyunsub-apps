package kim.hyunsub.photo.repository.condition

import kim.hyunsub.common.model.LocalDateRange
import org.springframework.data.domain.Pageable

data class PhotoCondition(
	val userId: String,
	val dateRange: LocalDateRange? = null,
	val page: Pageable,
)
