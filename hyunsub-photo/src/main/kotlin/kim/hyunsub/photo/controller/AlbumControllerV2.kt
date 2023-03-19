package kim.hyunsub.photo.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.model.RestApiAlbumCreateParams
import kim.hyunsub.photo.model.api.RestApiAlbumPreview
import kim.hyunsub.photo.model.api.RestApiAlbumV2
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumV2Repository
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.AlbumOwnerId
import kim.hyunsub.photo.repository.entity.AlbumV2
import kim.hyunsub.photo.repository.generateId
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/albums")
class AlbumControllerV2(
	private val albumRepository: AlbumV2Repository,
	private val albumOwnerRepository: AlbumOwnerRepository,
	private val albumPhotoRepository: AlbumPhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(userAuth: UserAuth): List<RestApiAlbumPreview> {
		val userId = userAuth.idNo
		log.debug { "[List Albums] userId=$userId" }

		return albumRepository.findByUserId(userId)
			.map { it.toPreview() }
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: RestApiAlbumCreateParams,
	): RestApiAlbumPreview {
		val userId = userAuth.idNo
		log.debug { "[Create Albums] userId=$userId, params=$params" }

		val album = AlbumV2(
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
	): RestApiAlbumV2 {
		val userId = userAuth.idNo
		log.debug { "[Detail Album] userId=$userId, albumId=$albumId" }

		albumOwnerRepository.findByIdOrNull(AlbumOwnerId(albumId, userId))
			?: run {
				log.debug { "[Detail Album] No such album: userId=$userId, albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val album = albumRepository.findByIdOrNull(albumId)
			?: run {
				log.debug { "[Detail Album] No such album: albumId=$albumId" }
				throw ErrorCodeException(ErrorCode.NOT_FOUND)
			}

		val photos = albumPhotoRepository.findByAlbumId(albumId).map { it.toPreview() }

		return RestApiAlbumV2(
			id = album.id,
			name = album.name,
			photos = photos,
		)
	}
}
