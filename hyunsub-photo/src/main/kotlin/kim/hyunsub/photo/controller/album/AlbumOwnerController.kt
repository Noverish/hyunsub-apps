package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumOwnerBo
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumOwnerParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/albums/{albumId}/owners")
class AlbumOwnerController(
	private val albumOwnerBo: AlbumOwnerBo,
) {
	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestBody params: AlbumOwnerParams,
	): ApiAlbum {
		return albumOwnerBo.create(userAuth.idNo, albumId, params)
	}

	@DeleteMapping("")
	fun delete(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@ModelAttribute params: AlbumOwnerParams,
	): ApiAlbum {
		return albumOwnerBo.delete(userAuth.idNo, albumId, params)
	}
}
