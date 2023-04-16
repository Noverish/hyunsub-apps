package kim.hyunsub.photo.model.api

data class ApiPhotoUploadResult(
	val success: Boolean,
	val nonce: String,
	val preview: ApiPhotoPreview? = null,
	val errMsg: String? = null,
) {
	companion object {
		fun success(nonce: String, preview: ApiPhotoPreview) = ApiPhotoUploadResult(
			success = true,
			nonce = nonce,
			preview = preview,
		)

		fun failure(nonce: String, ex: Exception) = ApiPhotoUploadResult(
			success = false,
			nonce = nonce,
			preview = null,
			errMsg = ex.message,
		)
	}
}
