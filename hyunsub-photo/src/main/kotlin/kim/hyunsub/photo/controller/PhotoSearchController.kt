package kim.hyunsub.photo.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.PhotoSearchBo
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
	): RestApiPageResult<ApiPhotoPreview> {
		return photoSearchBo.search(user.idNo, params)
	}
}
