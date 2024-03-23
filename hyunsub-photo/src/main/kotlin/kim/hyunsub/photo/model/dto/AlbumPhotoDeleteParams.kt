package kim.hyunsub.photo.model.dto

data class AlbumPhotoDeleteParams(
	val albumId: String,
	val photoIds: List<String>,
)
