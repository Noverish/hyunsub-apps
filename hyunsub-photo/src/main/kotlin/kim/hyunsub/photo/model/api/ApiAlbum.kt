package kim.hyunsub.photo.model.api

import kim.hyunsub.common.model.ApiPageResult

data class ApiAlbum(
	val id: String,
	val name: String,
	val photos: ApiPageResult<ApiPhotoPreview>,
)
