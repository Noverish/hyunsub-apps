package kim.hyunsub.photo.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.RestApiPhotoPreview
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import kim.hyunsub.photo.service.PhotoDeleteService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/photos")
class PhotoControllerV2(
	private val photoOwnerRepository: PhotoOwnerRepository,
	private val photoDeleteService: PhotoDeleteService,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam(required = false, defaultValue = "0") p: Int,
	): RestApiPageResult<RestApiPhotoPreview> {
		val userId = userAuth.idNo
		log.debug { "[List Photos] userId=$userId, p=$p" }

		val total = photoOwnerRepository.countByUserId(userId)

		val pageRequest = PageRequest.of(p, PhotoConstants.PHOTO_PAGE_SIZE)
		val data = photoOwnerRepository.selectMyPhotos(userId, pageRequest)
			.map { it.toPreview() }

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
			data = data,
		)
	}

	@DeleteMapping("/{photoId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable photoId: String,
	): RestApiPhotoPreview {
		val userId = userAuth.idNo
		log.debug { "[Delete Photo] userId=$userId, photoId=$photoId" }
		return photoDeleteService.delete(userId, photoId).toPreview()
	}
}
