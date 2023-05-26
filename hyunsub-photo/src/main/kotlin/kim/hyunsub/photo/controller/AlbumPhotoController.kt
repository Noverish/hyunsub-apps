package kim.hyunsub.photo.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.config.PhotoConstants
import kim.hyunsub.photo.model.api.ApiAlbumPhotoRegisterParams
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.ApiPhotoMetadata
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import kim.hyunsub.photo.service.PhotoDetailService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/albums/{albumId}/photos")
class AlbumPhotoController(
	private val albumRepository: AlbumRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val photoDetailService: PhotoDetailService,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestParam photoId: String?,
		@RequestParam p: Int?,
	): RestApiPageResult<ApiPhotoPreview> {
		val userId = userAuth.idNo
		log.debug { "[List Album Photos] userId=$userId, albumId=$albumId" }

		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val total = albumPhotoRepository.countByAlbumId(albumId)

		val page = when {
			photoId != null -> albumPhotoRepository.indexOfPhoto(albumId, photoId) / PhotoConstants.PAGE_SIZE
			else -> p ?: 0
		}

		val pageRequest = PageRequest.of(page, PhotoConstants.PAGE_SIZE)
		val data = albumPhotoRepository.findByAlbumId(albumId, pageRequest).map { it.toPreview() }

		return RestApiPageResult(
			total = total,
			page = page,
			pageSize = PhotoConstants.PAGE_SIZE,
			data = data,
		)
	}

	@GetMapping("/{photoId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@PathVariable photoId: String,
	): ApiPhoto {
		val userId = userAuth.idNo
		log.debug { "[Detail Photo] userId=$userId, albumId=$albumId photoId=$photoId" }
		return photoDetailService.detailInAlbum(userId, albumId, photoId)
	}

	@GetMapping("/metadata")
	fun metadataList(
		userAuth: UserAuth,
		@PathVariable albumId: String,
	): List<ApiPhotoMetadata> {
		val userId = userAuth.idNo
		log.debug { "[List Album Photos Metadata] userId=$userId, albumId=$albumId" }

		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return albumPhotoRepository.findPhotoDetailByAlbumId(albumId)
	}

	@PostMapping("")
	fun register(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestBody params: ApiAlbumPhotoRegisterParams,
	): List<AlbumPhoto> {
		val userId = userAuth.idNo
		log.debug { "[Register Album Photos] userId=$userId, albumId=$albumId, params=$params" }

		val album = albumRepository.findByAlbumIdAndUserId(albumId, userId)
			?: run {
				log.debug { "[Register Album Photos] No such album: userId=$userId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		if (album.thumbnailPhotoId == null) {
			val newAlbum = album.copy(thumbnailPhotoId = params.photoIds.random())
			albumRepository.save(newAlbum)
		}

		val albumPhotos = params.photoIds.map {
			AlbumPhoto(
				albumId = albumId,
				photoId = it,
				userId = userId,
			)
		}

		albumPhotoRepository.saveAll(albumPhotos)

		return albumPhotos
	}

	@DeleteMapping("/{photoId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@PathVariable photoId: String,
	): AlbumPhoto {
		val userId = userAuth.idNo
		log.debug { "[Delete Album Photos] userId=$userId, albumId=$albumId, photoId=$photoId" }

		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val albumPhoto = albumPhotoRepository.findByIdOrNull(AlbumPhotoId(albumId, photoId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		albumPhotoRepository.delete(albumPhoto)

		return albumPhoto
	}
}
