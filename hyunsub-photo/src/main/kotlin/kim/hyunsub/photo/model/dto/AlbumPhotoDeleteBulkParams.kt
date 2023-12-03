package kim.hyunsub.photo.model.dto

data class AlbumPhotoDeleteBulkParams(
	val albumId: String,
	val photoIds: List<String>,
)
