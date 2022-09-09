package kim.hyunsub.photo.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.RestApiAlbum
import kim.hyunsub.photo.model.RestApiAlbumDetail
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.service.ApiModelConverter
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_photo"])
@RestController
@RequestMapping("/api/v1/album")
class AlbumController(
	private val albumRepository: AlbumRepository,
	private val photoRepository: PhotoRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	companion object : Log

	@GetMapping("")
	fun list(): List<RestApiAlbum> {
		return albumRepository.findAll()
			.sortedBy { it.name }
			.map { apiModelConverter.convert(it) }
	}

	@GetMapping("/{albumId}")
	fun detail(@PathVariable albumId: Int): RestApiAlbumDetail {
		val album = albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val page = PageRequest.of(0, PhotoConstants.PHOTO_PAGE_SIZE)
		val photos = photoRepository.findByAlbumId(albumId, page)

		return RestApiAlbumDetail(
			album = apiModelConverter.convert(album),
			photos = photos.map { apiModelConverter.convert(it) },
		)
	}

	@GetMapping("/{albumId}/photos")
	fun photos(
		@PathVariable albumId: Int,
		p: Int,
	): List<RestApiPhoto> {
		albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val page = PageRequest.of(p, PhotoConstants.PHOTO_PAGE_SIZE)
		val photos = photoRepository.findByAlbumId(albumId, page)
		return photos.map { apiModelConverter.convert(it) }
	}
}
