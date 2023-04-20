package kim.hyunsub.photo.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.api.ApiAlbumCreateParams
import kim.hyunsub.photo.model.api.ApiAlbumPreview
import kim.hyunsub.photo.model.api.ApiAlbumThumbnailParams
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.entity.Album
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.Photo
import kim.hyunsub.photo.repository.generateId
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/albums")
class AlbumController(
	private val albumRepository: AlbumRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
	): RestApiPageResult<ApiAlbumPreview> {
		val userId = userAuth.idNo
		log.debug { "[List Albums] userId=$userId" }

		val total = albumOwnerRepository.countByUserId(userId)

		val page = PageRequest.of(p, PhotoConstants.PHOTO_PAGE_SIZE)
		val data = albumRepository.findByUserId(userId, page).map { it.toPreview() }

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
			data = data,
		)
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: ApiAlbumCreateParams,
	): ApiAlbumPreview {
		val userId = userAuth.idNo
		log.debug { "[Create Albums] userId=$userId, params=$params" }

		val album = Album(
			id = albumRepository.generateId(),
			name = params.name,
		)

		val albumOwner = AlbumOwner(
			albumId = album.id,
			userId = userId,
			owner = true,
		)

		albumRepository.save(album)
		albumOwnerRepository.save(albumOwner)

		return album.toPreview()
	}

	@GetMapping("/{albumId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable albumId: String,
	): ApiAlbum {
		val userId = userAuth.idNo
		log.debug { "[Detail Album] userId=$userId, albumId=$albumId" }

		val album = albumRepository.findByAlbumIdAndUserId(albumId, userId)
			?: run {
				log.debug { "[Detail Album] No such album: userId=$userId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val total = albumPhotoRepository.countByAlbumId(albumId)

		val page = PageRequest.ofSize(PhotoConstants.PHOTO_PAGE_SIZE)
		val photos = albumPhotoRepository.findByAlbumId(albumId, page).map { it.toPreview() }

		return ApiAlbum(
			id = album.id,
			name = album.name,
			photos = RestApiPageResult(
				total = total,
				page = 0,
				pageSize = PhotoConstants.PHOTO_PAGE_SIZE,
				data = photos,
			),
		)
	}

	@PostMapping("/{albumId}/thumbnail")
	fun registerThumbnail(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestBody params: ApiAlbumThumbnailParams,
	) : ApiAlbumPreview {
		val userId = userAuth.idNo
		log.debug { "[Album Thumbnail] userId=$userId, albumId=$albumId, params=$params" }

		val album = albumRepository.findByAlbumIdAndUserId(albumId, userId)
			?: run {
				log.debug { "[Album Thumbnail] No such album: userId=$userId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val photoId = params.photoId
		albumPhotoRepository.findByAlbumIdAndPhotoId(albumId, photoId).firstOrNull()
			?: run {
				log.debug { "[Album Thumbnail] No such photo: albumId=$albumId, photoId=$photoId" }
				throw ErrorCodeException(ErrorCode.NO_SUCH_PHOTO)
			}

		val newAlbum = album.copy(thumbnailPhotoId = photoId)

		albumRepository.save(newAlbum)

		return newAlbum.toPreview()
	}
}
