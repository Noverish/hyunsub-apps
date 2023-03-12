package kim.hyunsub.photo.model.dto

import kim.hyunsub.photo.model.api.RestApiPhotoPreview

data class PhotoUploadResult(
	val success: Boolean,
	val nonce: String,
	val preview: RestApiPhotoPreview? = null,
	val errMsg: String? = null,
) {
	companion object {
		fun success(nonce: String, preview: RestApiPhotoPreview) = PhotoUploadResult(
			success = true,
			nonce = nonce,
			preview = preview,
		)

		fun failure(nonce: String, ex: Exception) = PhotoUploadResult(
			success = false,
			nonce = nonce,
			preview = null,
			errMsg = ex.message,
		)
	}
}
