package kim.hyunsub.photo.model.api

data class RestApiAlbumV2(
	val id: String,
	val name: String,
	val photos: List<RestApiPhotoPreview>,
)
