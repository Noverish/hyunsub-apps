package kim.hyunsub.photo.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.RestApiAlbum
import kim.hyunsub.photo.model.RestApiAlbumPreview
import kim.hyunsub.photo.model.RestApiExifDate
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.service.ApiModelConverter
import kim.hyunsub.photo.service.PhotoMetadataDateParser
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@Authorized(authorities = ["service_photo"])
@RestController
@RequestMapping("/api/v1/albums")
class AlbumController(
	private val albumRepository: AlbumRepository,
	private val photoRepository: PhotoRepository,
	private val photoMetadataRepository: PhotoMetadataRepository,
	private val apiModelConverter: ApiModelConverter,
	private val photoMetadataDateParser: PhotoMetadataDateParser,
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
		val photos = photoRepository.findByAlbumIdOrderByDate(albumId, pageRequest)
			.map { apiModelConverter.convert(it) }

		return RestApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
			data = photos,
		)
	}

	@GetMapping("/{albumId}/exif/date")
	fun exifDate(
		@PathVariable albumId: Int,
		@RequestParam p: Int,
	): RestApiPageResult<RestApiExifDate> {
		albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = photoRepository.countByAlbumId(albumId)
		val pageRequest = PageRequest.of(p, PhotoConstants.PHOTO_PAGE_SIZE)
		val photos = photoRepository.findByAlbumIdOrderByDate(albumId, pageRequest)
		val list = photoMetadataRepository.findAllById(photos.map { it.path })
			.sortedBy { it.date }
			.map { metadata ->
				val photo = photos.first { it.path == metadata.path }
				photoMetadataDateParser.parse(photo, metadata)
			}

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
			data = list,
		)
	}
}
