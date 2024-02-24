package kim.hyunsub.photo.controller.admin

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.api.ApiRescanAlbumDateParams
import kim.hyunsub.photo.model.api.ApiRescanAlbumDateResult
import kim.hyunsub.photo.model.api.ApiRescanPhotoDateParams
import kim.hyunsub.photo.model.api.ApiRescanPhotoDateResult
import kim.hyunsub.photo.model.api.ApiUpdateAlbumOffsetParams
import kim.hyunsub.photo.model.api.ApiUpdatePhotoOffsetParams
import kim.hyunsub.photo.repository.mapper.AlbumMapper
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import kim.hyunsub.photo.repository.mapper.generateId
import kim.hyunsub.photo.service.PhotoUpdateService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/v1/admin")
class PhotoAdminController(
	private val photoMapper: PhotoMapper,
	private val photoUpdateService: PhotoUpdateService,
	private val albumMapper: AlbumMapper,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/rescan-photo-date")
	fun rescanPhotoDate(@RequestBody params: ApiRescanPhotoDateParams): ApiRescanPhotoDateResult {
		val result = photoUpdateService.rescanPhotoDate(params.photoId)
		return ApiRescanPhotoDateResult(result)
	}

	@PostMapping("/rescan-album-date")
	fun rescanAlbumDate(@RequestBody params: ApiRescanAlbumDateParams): ApiRescanAlbumDateResult {
		albumMapper.selectOne(params.albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photos = photoMapper.selectByAlbumId(params.albumId)
		val results = photos.mapNotNull { photoUpdateService.rescanPhotoDate(it.id) }
		return ApiRescanAlbumDateResult(results)
	}

	@PostMapping("/update-offset-same-local")
	fun updateOffsetSameLocal(@RequestBody params: ApiUpdatePhotoOffsetParams): SimpleResponse {
		val photo = photoMapper.selectOne(params.photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newDate = photo.date.withOffsetSameLocal(ZoneOffset.ofHours(params.hour))

		val newId = photoMapper.generateId(newDate, photo.hash)
		log.debug { "[Rescan Photo Date] ${photo.id} -> $newId : ${photo.date} -> $newDate" }
		photoUpdateService.updateId(photo, newId)

		val newPhoto = photo.copy(id = newId, offset = params.hour * 3600)
		photoMapper.insert(newPhoto)

		return SimpleResponse()
	}

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
