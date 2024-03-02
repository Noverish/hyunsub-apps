package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.photo.PhotoOwnerBo
import kim.hyunsub.photo.model.api.ApiUpdatePhotoOffsetParams
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class PhotoOwnerController(
	private val photoOwnerBo: PhotoOwnerBo,
) {
	@PostMapping("/update-offset-same-local")
	fun updateOffsetSameLocal(
		userAuth: UserAuth,
		@RequestBody params: ApiUpdatePhotoOffsetParams,
	): SimpleResponse {
		return photoOwnerBo.updateOffsetSameLocal(userAuth.idNo, params)
	}

	@PostMapping("/update-offset-same-instant")
	fun updateOffsetSameInstant(
		userAuth: UserAuth,
		@RequestBody params: ApiUpdatePhotoOffsetParams,
	): SimpleResponse {
		return photoOwnerBo.updateOffsetSameInstant(userAuth.idNo, params)
	}
}
