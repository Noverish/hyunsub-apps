package kim.hyunsub.photo.repository.entity

data class AlbumOwnerWithName(
	val albumId: String,
	val userId: String,
	val owner: Boolean,
	val username: String,
)
