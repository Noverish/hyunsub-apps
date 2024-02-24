package kim.hyunsub.photo.repository.condition

import kim.hyunsub.common.model.StringRange
import org.springframework.data.domain.Pageable

data class PhotoCondition(
	val userId: String? = null,
	val idRange: StringRange? = null,
	val idGreaterThan: String? = null,
	val idLessThan: String? = null,
	val hash: String? = null,
	val page: Pageable? = null,
	val asc: Boolean = false,
)
