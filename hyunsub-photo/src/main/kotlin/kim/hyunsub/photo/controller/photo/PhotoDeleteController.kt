package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.photo.PhotoDeleteBo
import kim.hyunsub.photo.model.dto.PhotoDeleteParams
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PhotoDeleteController(
	private val photoDeleteBo: PhotoDeleteBo,
) {
	@PostMapping("/api/v1/_bulk/photos/delete")
	fun deleteBulk(
		userAuth: UserAuth,
		@RequestBody params: PhotoDeleteParams,
	): SimpleResponse {
		return photoDeleteBo.delete(userAuth.idNo, params)
	}
}
