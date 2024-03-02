package kim.hyunsub.photo.controller.admin

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.api.ApiUpdateAlbumOffsetParams
import kim.hyunsub.photo.model.api.ApiUpdatePhotoOffsetParams
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class PhotoAdminController(
	private val photoMapper: PhotoMapper,
) {
	@PostMapping("/update-offset-same-instant")
	fun updateOffsetSameInstant(@RequestBody params: ApiUpdatePhotoOffsetParams): SimpleResponse {
		val photo = photoMapper.selectOne(params.photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newPhoto = photo.copy(offset = params.hour * 3600)
		photoMapper.insert(newPhoto)

		return SimpleResponse()
	}

	@PostMapping("/update-offset-same-instant-of-album")
	fun updateOffsetSameInstantOfAlbum(@RequestBody params: ApiUpdateAlbumOffsetParams): SimpleResponse {
		photoMapper.selectOne(params.albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photos = photoMapper.selectByAlbumId(params.albumId)

		val newPhotos = photos.map { it.copy(offset = params.hour * 3600) }

		photoMapper.insertAll(newPhotos)

		return SimpleResponse()
	}
}
