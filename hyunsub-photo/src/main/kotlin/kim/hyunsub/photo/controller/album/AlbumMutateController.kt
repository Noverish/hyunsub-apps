package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumMutateBo
import kim.hyunsub.photo.model.api.ApiAlbum
import kim.hyunsub.photo.model.dto.AlbumCreateParams
import kim.hyunsub.photo.model.dto.AlbumThumbnailParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/albums")
class AlbumMutateController(
	private val albumMutateBo: AlbumMutateBo,
) {
	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: AlbumCreateParams,
	): ApiAlbum {
		return albumMutateBo.create(userAuth.idNo, params)
	}

	@DeleteMapping("/{albumId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable albumId: String,
	): ApiAlbum {
		return albumMutateBo.delete(userAuth.idNo, albumId)
	}

	@PostMapping("/{albumId}/thumbnail")
	fun updateThumbnail(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestBody params: AlbumThumbnailParams,
	): ApiAlbum {
		return albumMutateBo.updateThumbnail(userAuth.idNo, albumId, params)
	}
}
