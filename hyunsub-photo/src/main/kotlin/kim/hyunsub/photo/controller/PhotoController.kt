package kim.hyunsub.photo.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.service.ApiModelConverter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_photo"])
@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
	private val photoRepository: PhotoRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("/{photoId}")
	fun detail(@PathVariable photoId: Int): RestApiPhoto {
		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return apiModelConverter.convert(photo)
	}
}
