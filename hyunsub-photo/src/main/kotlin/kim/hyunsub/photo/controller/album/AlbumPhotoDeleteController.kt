package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumPhotoDeleteBo
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.model.dto.AlbumPhotoDeleteBulkParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AlbumPhotoDeleteController(
	private val albumPhotoDeleteBo: AlbumPhotoDeleteBo,
) {
	@DeleteMapping("/api/v1/albums/{albumId}/photos/{photoId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@PathVariable photoId: String,
	): ApiPhotoPreview {
		return albumPhotoDeleteBo.delete(userAuth.idNo, albumId, photoId)
	}

	@PostMapping("/api/v1/_bulk/albums/photos/delete")
	fun deleteBulk(
		userAuth: UserAuth,
		@RequestBody params: AlbumPhotoDeleteBulkParams,
	): List<ApiPhotoPreview> {
		return albumPhotoDeleteBo.deleteBulk(userAuth.idNo, params)
	}
}
