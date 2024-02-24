package kim.hyunsub.photo.repository.entity

data class AlbumOwner(
	val albumId: String,
	val userId: String,
	val owner: Boolean,
)
