package kim.hyunsub.photo.model.api

import kim.hyunsub.common.model.RestApiPageResult

data class ApiAlbum(
	val id: String,
	val name: String,
	val photos: RestApiPageResult<ApiPhotoPreview>,
)