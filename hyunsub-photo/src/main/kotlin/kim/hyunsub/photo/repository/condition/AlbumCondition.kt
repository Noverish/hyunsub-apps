package kim.hyunsub.photo.repository.condition

import org.springframework.data.domain.Pageable

data class AlbumCondition(
	val userId: String? = null,
	val owner: Boolean? = null,
	val thumbnailPhotoId: String? = null,
	val page: Pageable? = null,
)
