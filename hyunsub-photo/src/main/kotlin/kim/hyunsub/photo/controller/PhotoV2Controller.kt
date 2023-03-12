package kim.hyunsub.photo.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.model.api.RestApiPhotoPreview
import kim.hyunsub.photo.repository.PhotoOwnerRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/photos")
class PhotoV2Controller(
	private val photoOwnerRepository: PhotoOwnerRepository,
) {
	@GetMapping("")
	fun detail(userAuth: UserAuth): List<RestApiPhotoPreview> {
		val userId = userAuth.idNo
		return photoOwnerRepository.selectMyPhotos(userId)
			.map { it.toPreview() }
	}
}
