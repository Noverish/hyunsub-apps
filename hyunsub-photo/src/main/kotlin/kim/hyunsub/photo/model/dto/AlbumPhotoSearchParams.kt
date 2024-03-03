package kim.hyunsub.photo.model.dto

data class AlbumPhotoSearchParams(
	val page: Int = 0,
	val photoId: String? = null,
	val userIds: List<String>? = null,
)
