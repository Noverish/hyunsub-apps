package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.photo.PhotoDeleteBo
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.dto.PhotoDeleteBulkParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PhotoDeleteController(
	private val photoDeleteBo: PhotoDeleteBo,
) {
	@DeleteMapping("/api/v1/photos/{photoId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable photoId: String,
	): ApiPhoto {
		return photoDeleteBo.delete(userAuth.idNo, photoId)
	}

	@PostMapping("/api/v1/_bulk/photos/delete")
	fun deleteBulk(
		userAuth: UserAuth,
		@RequestBody params: PhotoDeleteBulkParams,
	): List<ApiPhoto> {
		return photoDeleteBo.deleteBulk(userAuth.idNo, params)
	}
}
