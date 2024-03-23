package kim.hyunsub.photo.repository.condition

import org.springframework.data.domain.Pageable

data class AlbumPhotoCondition(
	val albumId: String? = null,
	val albumIds: List<String>? = null,
	val excludePhotoIds: List<String>? = null,
	val page: Pageable? = null,
)
