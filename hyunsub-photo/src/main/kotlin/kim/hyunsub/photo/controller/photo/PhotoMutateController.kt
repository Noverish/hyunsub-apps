package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.photo.PhotoMutateBo
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.dto.PhotoDateUpdateParams
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/photos/{photoId}")
class PhotoMutateController(
	private val photoMutateBo: PhotoMutateBo,
) {
	@PutMapping("/date")
	fun updatePhotoDate(
		userAuth: UserAuth,
		@PathVariable photoId: String,
		@RequestBody params: PhotoDateUpdateParams,
	): ApiPhoto {
		return photoMutateBo.updatePhotoDate(userAuth.idNo, photoId, params)
	}
}
