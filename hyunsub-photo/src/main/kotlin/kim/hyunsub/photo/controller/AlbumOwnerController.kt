package kim.hyunsub.photo.controller

import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.dto.AlbumOwnerCreateParams
import kim.hyunsub.photo.repository.AlbumOwnerRepository
import kim.hyunsub.photo.repository.entity.AlbumOwner
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/albums/{albumId}/owners")
class AlbumOwnerController(
	private val albumOwnerRepository: AlbumOwnerRepository,
) {
	@PostMapping("")
	fun register(
		@PathVariable albumId: String,
		@RequestBody params: AlbumOwnerCreateParams,
	): SimpleResponse {
		val albumOwner = AlbumOwner(albumId, params.userId, false)
		albumOwnerRepository.save(albumOwner)
		return SimpleResponse()
	}
}
