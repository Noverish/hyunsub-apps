package kim.hyunsub.photo.repository.condition

import org.springframework.data.domain.Pageable

data class PhotoCondition2(
	val albumId: String,
	val userIds: List<String>? = null,
	val page: Pageable,
)
