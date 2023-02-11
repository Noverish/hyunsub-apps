package kim.hyunsub.photo.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.RestApiAlbum
import kim.hyunsub.photo.model.RestApiAlbumCreateParams
import kim.hyunsub.photo.model.RestApiAlbumPreview
import kim.hyunsub.photo.model.RestApiPhoto
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.service.ApiModelConverter
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/v1/albums")
class AlbumController(
	private val albumRepository: AlbumRepository,
	private val photoRepository: PhotoRepository,
	private val apiModelConverter: ApiModelConverter,
	private val apiCaller: ApiCaller,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(): List<RestApiAlbumPreview> {
		return albumRepository.findAll()
			.sortedBy { it.name }
			.map { apiModelConverter.convertToPreview(it) }
	}

	@Authorized(["admin"])
	@PostMapping("")
	fun create(@RequestBody params: RestApiAlbumCreateParams): RestApiAlbumPreview {
		val dirPath = Path(PhotoConstants.basePath, params.name).toString()
		apiCaller.mkdir(dirPath)

		val album = Album(
			name = params.name,
		)
		albumRepository.saveAndFlush(album)
		return apiModelConverter.convertToPreview(album)
	}

	@GetMapping("/{albumId}")
	fun detail(@PathVariable albumId: Int): RestApiAlbum {
		val album = albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = photoRepository.countByAlbumId(albumId)

		return apiModelConverter.convert(album, total)
	}

	@GetMapping("/{albumId}/photos")
	fun photos(
		@PathVariable albumId: Int,
		@RequestParam(required = false) p: Int?,
		@RequestParam(required = false) photoId: Int?,
	): RestApiPageResult<RestApiPhoto> {
		log.debug { "[Album Photos] albumId=$albumId, p=$p, photoId=$photoId" }

		val album = albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug { "[Album Photos] album=$album" }

		val total = photoRepository.countByAlbumId(albumId)
		log.debug { "[Album Photos] total=$total" }

		val page =
			if (photoId != null) {
				val ids = photoRepository.findIdByAlbumIdOrderByDate(albumId)
				val index = ids.indexOf(photoId)
				index / PhotoConstants.PHOTO_PAGE_SIZE
			} else {
				p ?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER)
			}
		log.debug { "[Album Photos] page=$page" }

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
}
