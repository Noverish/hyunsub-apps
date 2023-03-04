package kim.hyunsub.photo.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.RestApiExifDate
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.service.PhotoMetadataDateParser
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class PhotoDateController(
	private val albumRepository: AlbumRepository,
	private val photoRepository: PhotoRepository,
	private val photoMetadataRepository: PhotoMetadataRepository,
	private val photoMetadataDateParser: PhotoMetadataDateParser,
) {
	@GetMapping("/albums/{albumId}/photos/date")
	fun albumPhotoDates(
		@PathVariable albumId: Int,
		@RequestParam p: Int,
	): RestApiPageResult<RestApiExifDate> {
		albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = photoRepository.countByAlbumId(albumId)
		val pageRequest = PageRequest.of(p, PhotoConstants.PHOTO_PAGE_SIZE)
		val photos = photoRepository.findByAlbumIdOrderByDate(albumId, pageRequest)
		val list = photoMetadataRepository.findAllById(photos.map { it.path })
			.map { metadata ->
				val photo = photos.first { it.path == metadata.path }
				photoMetadataDateParser.parse(photo, metadata)
			}
			.sortedBy { it.date }

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
			data = list,
		)
	}
}
