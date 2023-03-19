package kim.hyunsub.photo.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.model.RestApiAlbumCreateParams
import kim.hyunsub.photo.model.api.RestApiAlbumPreview
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.AlbumV2Repository
import kim.hyunsub.photo.repository.entity.AlbumOwner
import kim.hyunsub.photo.repository.entity.AlbumV2
import kim.hyunsub.photo.repository.generateId
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/albums")
class AlbumControllerV2(
	private val albumRepository: AlbumV2Repository,
	private val albumOwnerRepository: AlbumOwnerRepository,
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
}
