package kim.hyunsub.photo.controller.photo

import kim.hyunsub.common.model.ApiPagination
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.photo.PhotoDetailBo
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.service.PhotoListService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
	private val photoDetailBo: PhotoDetailBo,
	private val photoListService: PhotoListService,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam photoId: String?,
		@RequestParam prev: String?,
		@RequestParam next: String?,
	): ApiPagination<ApiPhotoPreview> {
		val userId = userAuth.idNo
		log.debug { "[List Photos] userId=$userId, photoId=$photoId, prev=$prev, next=$next" }

		return when {
			photoId != null -> photoListService.listPhotoWithPhotoId(userId, photoId)
			next != null -> photoListService.list(userId, next = next)
			prev != null -> photoListService.list(userId, prev = prev)
			else -> photoListService.list(userId)
		}
	}

	@GetMapping("/{photoId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable photoId: String,
		@RequestParam(required = false) albumId: String?,
	): ApiPhoto {
		return photoDetailBo.detail(userAuth.idNo, photoId, albumId)
	}
}
