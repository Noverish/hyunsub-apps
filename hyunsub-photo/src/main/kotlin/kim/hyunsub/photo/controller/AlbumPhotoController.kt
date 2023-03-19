package kim.hyunsub.photo.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.model.dto.AlbumPhotoRegisterParams
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import kim.hyunsub.photo.repository.entity.AlbumPhoto
import kim.hyunsub.photo.repository.entity.AlbumPhotoId
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/albums/{albumId}/photos")
class AlbumPhotoController(
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumOwnerRepository: AlbumOwnerRepository,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable albumId: String,
	): List<AlbumPhoto> {
		val userId = userAuth.idNo
		log.debug { "[List Album Photos] userId=$userId, albumId=$albumId" }

		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return albumPhotoRepository.findByAlbumId(albumId)
	}

	@PostMapping("")
	fun register(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestBody params: AlbumPhotoRegisterParams,
	): List<AlbumPhoto> {
		val userId = userAuth.idNo
		log.debug { "[Register Album Photos] userId=$userId, albumId=$albumId, params=$params" }

		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: run {
				log.debug { "[Register Album Photos] No such album: userId=$userId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
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
