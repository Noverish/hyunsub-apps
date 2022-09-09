package kim.hyunsub.photo.model

data class RestApiAlbumDetail(
	val album: RestApiAlbum,
	val photos: List<RestApiPhoto>,
)
