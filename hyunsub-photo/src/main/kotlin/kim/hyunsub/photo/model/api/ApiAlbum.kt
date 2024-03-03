package kim.hyunsub.photo.model.api

data class ApiAlbum(
	val id: String,
	val name: String,
	val total: Int,
	val members: List<ApiAlbumMember>,
)
