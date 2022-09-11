package kim.hyunsub.photo.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.RestApiAlbum
import kim.hyunsub.photo.model.RestApiAlbumPreview
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.service.ApiModelConverter
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@Authorized(authorities = ["service_photo"])
@RestController
@RequestMapping("/api/v1/albums")
class AlbumController(
	private val albumRepository: AlbumRepository,
	private val photoRepository: PhotoRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun list(): List<RestApiAlbumPreview> {
		return albumRepository.findAll()
			.sortedBy { it.name }
			.map { apiModelConverter.convertToPreview(it) }
	}

	@GetMapping("/{albumId}")
	fun detail(@PathVariable albumId: Int): RestApiAlbum {
		val album = albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = photoRepository.countByAlbumId(albumId)

		return RestApiAlbum(
			preview = apiModelConverter.convertToPreview(album),
			total = total,
		)
	}

	@GetMapping("/{albumId}/photos")
	fun photos(
		@PathVariable albumId: Int,
		@RequestParam(required = false) p: Int?,
		@RequestParam(required = false) photoId: Int?,
	): RestApiPageResult<RestApiPhoto> {
		albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = photoRepository.countByAlbumId(albumId)
		val page =
			if (p != null && photoId == null) {
				p
			} else if (p == null && photoId != null) {
				val idx = photoRepository.getIndexOfPhotoIdInAlbum(albumId, photoId)
					?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
				idx / PhotoConstants.PHOTO_PAGE_SIZE
			} else {
				throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
			}

		val pageRequest = PageRequest.of(page, PhotoConstants.PHOTO_PAGE_SIZE)
		val photos = photoRepository.findByAlbumId(albumId, pageRequest)
			.map { apiModelConverter.convert(it) }

		return RestApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
			data = photos,
		)
	}
}
