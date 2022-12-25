package kim.hyunsub.photo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.util.convertToMap
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import kim.hyunsub.photo.service.ApiModelConverter
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/photos")
class PhotoController(
	private val photoRepository: PhotoRepository,
	private val photoMetadataRepository: PhotoMetadataRepository,
	private val apiModelConverter: ApiModelConverter,
	private val mapper: ObjectMapper,
	private val apiCaller: ApiCaller,
) {
	companion object : Log

	@GetMapping("/{photoId}")
	fun detail(@PathVariable photoId: Int): RestApiPhoto {
		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return apiModelConverter.convert(photo)
	}

	@GetMapping("/{photoId}/exif")
	fun exif(@PathVariable photoId: Int): String {
		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val metadata = photoMetadataRepository.findByIdOrNull(photo.path)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return metadata.data
	}

	@PostMapping("/{photoId}/exif/reload")
	fun reloadExif(@PathVariable photoId: Int): String {
		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val exif = apiCaller.exif(photo.path)
		val metadata = PhotoMetadata(
			path = photo.path,
			data = exif,
			date = LocalDateTime.now(),
		)

		photoMetadataRepository.saveAndFlush(metadata)

		val newPhoto = photo.update(metadata)
		photoRepository.saveAndFlush(newPhoto)

		return metadata.data
	}

	@Authorized(authorities = ["admin"])
	@PutMapping("/{photoId}")
	fun update(
		@PathVariable photoId: Int,
		@RequestBody body: Map<String, Any>,
	): RestApiPhoto {
		val photo = photoRepository.findByIdOrNull(photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val origin = mapper.convertToMap(photo)
		val merged: Photo = mapper.convertValue(origin + body)
		photoRepository.save(merged)

		return apiModelConverter.convert(photo)
	}
}
