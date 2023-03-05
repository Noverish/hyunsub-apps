package kim.hyunsub.photo.model.dto

data class PhotoUploadResult(
	val success: Boolean,
	val path: String? = null,
	val errMsg: String? = null,
) {
	companion object {
		fun success(path: String) = PhotoUploadResult(
			success = true,
			path = path,
		)

		fun failure(path: String, ex: Exception) = PhotoUploadResult(
			success = false,
			path = path,
			errMsg = ex.message,
		)
	}
}
