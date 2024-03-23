package kim.hyunsub.photo.repository.condition

import org.springframework.data.domain.Pageable

data class PhotoOwnerCondition(
	val userId: String? = null,
	val photoId: String? = null,
	val photoIds: List<String>? = null,
	val name: String? = null,
	val page: Pageable? = null,
)
