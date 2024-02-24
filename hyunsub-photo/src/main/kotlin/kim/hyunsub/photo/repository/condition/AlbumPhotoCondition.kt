package kim.hyunsub.photo.repository.condition

data class AlbumPhotoCondition(
	val albumId: String? = null,
	val photoId: String? = null,
	val userId: String? = null,
	val albumIds: List<String>? = null,
)
