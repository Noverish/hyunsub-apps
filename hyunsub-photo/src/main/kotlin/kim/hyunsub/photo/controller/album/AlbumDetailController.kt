package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumDetailBo
import kim.hyunsub.photo.model.api.ApiAlbum
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/albums")
class AlbumDetailController(
	private val albumDetailBo: AlbumDetailBo,
) {
	@GetMapping("/{albumId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable albumId: String,
	): ApiAlbum? {
		return albumDetailBo.detail(userAuth.idNo, albumId)
	}
}
