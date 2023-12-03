package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.photo.PhotoSearchBo
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.dto.PhotoSearchParams
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/search/photos")
class PhotoSearchController(
	private val photoSearchBo: PhotoSearchBo,
) {
	@PostMapping("")
	fun search(
		user: UserAuth,
		@RequestBody params: PhotoSearchParams,
	): ApiPageResult<ApiPhotoPreview> {
		return photoSearchBo.search(user.idNo, params)
	}
}
