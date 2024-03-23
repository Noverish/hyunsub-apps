package kim.hyunsub.photo.repository.condition

import org.springframework.data.domain.Pageable

data class PhotoOfAlbumCondition(
	val albumId: String,
	val userIds: List<String>? = null,
	val page: Pageable? = null,
)
