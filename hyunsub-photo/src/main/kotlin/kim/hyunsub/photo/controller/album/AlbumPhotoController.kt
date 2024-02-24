package kim.hyunsub.photo.controller.album

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.photo.bo.album.AlbumPhotoBo
import kim.hyunsub.photo.bo.photo.PhotoDetailBo
import kim.hyunsub.photo.model.api.ApiPhoto
import kim.hyunsub.photo.model.api.ApiPhotoPreview
import kim.hyunsub.photo.repository.entity.ApiPhotoMetadata
import kim.hyunsub.photo.repository.mapper.AlbumOwnerMapper
import kim.hyunsub.photo.repository.mapper.PhotoMetadataMapper
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/albums/{albumId}/photos")
class AlbumPhotoController(
	private val photoDetailBo: PhotoDetailBo,
	private val albumOwnerMapper: AlbumOwnerMapper,
	private val albumPhotoBo: AlbumPhotoBo,
	private val photoMetadataMapper: PhotoMetadataMapper,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@RequestParam photoId: String?,
		@RequestParam p: Int?,
	): ApiPageResult<ApiPhotoPreview> {
		return albumPhotoBo.list(userAuth.idNo, albumId, p, photoId)
	}

	@GetMapping("/{photoId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable albumId: String,
		@PathVariable photoId: String,
	): ApiPhoto {
		return photoDetailBo.detail(userAuth.idNo, photoId, albumId)
	}

	@GetMapping("/metadata")
	fun metadataList(
		userAuth: UserAuth,
		@PathVariable albumId: String,
	): List<ApiPhotoMetadata> {
		val userId = userAuth.idNo
		log.debug { "[List Album Photos Metadata] userId=$userId, albumId=$albumId" }

		albumOwnerMapper.selectOne(albumId = albumId, userId = userId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return photoMetadataMapper.selectDetailByAlbumId(albumId)
	}
}
