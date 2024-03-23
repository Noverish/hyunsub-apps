package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumPhotoMutateBo
import kim.hyunsub.photo.model.dto.AlbumPhotoCreateParams
import kim.hyunsub.photo.model.dto.AlbumPhotoDeleteParams
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AlbumPhotoMutateController(
	private val albumPhotoMutateBo: AlbumPhotoMutateBo,
) {
	@PostMapping("/api/v1/albums/{albumId}/photos")
	fun create(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestBody params: AlbumPhotoCreateParams,
	): SimpleResponse {
		return albumPhotoMutateBo.create(userAuth.idNo, albumId, params)
	}

	@PostMapping("/api/v1/_bulk/albums/photos/delete")
	fun delete(
		userAuth: UserAuth,
		@RequestBody params: AlbumPhotoDeleteParams,
	): SimpleResponse {
		return albumPhotoMutateBo.delete(userAuth.idNo, params)
	}
}
