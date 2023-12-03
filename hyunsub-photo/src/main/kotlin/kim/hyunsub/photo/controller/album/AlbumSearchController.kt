package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumSearchBo
import kim.hyunsub.photo.model.api.ApiAlbumPreview
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/albums")
class AlbumSearchController(
	private val albumSearchBo: AlbumSearchBo,
) {
	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@RequestParam(defaultValue = "0") p: Int,
	): ApiPageResult<ApiAlbumPreview> {
		return albumSearchBo.search(userAuth.idNo, p)
	}
}
