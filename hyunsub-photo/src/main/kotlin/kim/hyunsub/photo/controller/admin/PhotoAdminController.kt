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
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.service.PhotoUpdateService
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/v1/admin")
class PhotoAdminController(
	private val photoRepository: PhotoRepository,
	private val photoUpdateService: PhotoUpdateService,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val albumRepository: AlbumRepository,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/rescan-photo-date")
	fun rescanPhotoDate(@RequestBody params: ApiRescanPhotoDateParams): ApiRescanPhotoDateResult {
		val result = photoUpdateService.rescanPhotoDate(params.photoId)
		return ApiRescanPhotoDateResult(result)
	}

	@PostMapping("/rescan-album-date")
	fun rescanAlbumDate(@RequestBody params: ApiRescanAlbumDateParams): ApiRescanAlbumDateResult {
		albumRepository.findByIdOrNull(params.albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photos = albumPhotoRepository.findByAlbumId(params.albumId)
		val results = photos.mapNotNull { photoUpdateService.rescanPhotoDate(it.id) }
		return ApiRescanAlbumDateResult(results)
	}

	@PostMapping("/update-offset-same-local")
	fun updateOffsetSameLocal(@RequestBody params: ApiUpdatePhotoOffsetParams): SimpleResponse {
		val photo = photoRepository.findByIdOrNull(params.photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newDate = photo.date.withOffsetSameLocal(ZoneOffset.ofHours(params.hour))

		val newId = photoRepository.generateId(newDate, photo.hash)
		log.debug { "[Rescan Photo Date] ${photo.id} -> $newId : ${photo.date} -> $newDate" }
		photoUpdateService.updateId(photo, newId)

		val newPhoto = photo.copy(id = newId, offset = params.hour * 3600)
		photoRepository.save(newPhoto)

		return SimpleResponse()
	}

	@PostMapping("/update-offset-same-instant")
	fun updateOffsetSameInstant(@RequestBody params: ApiUpdatePhotoOffsetParams): SimpleResponse {
		val photo = photoRepository.findByIdOrNull(params.photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val newPhoto = photo.copy(offset = params.hour * 3600)
		photoRepository.save(newPhoto)

		return SimpleResponse()
	}

	@PostMapping("/update-offset-same-instant-of-album")
	fun updateOffsetSameInstantOfAlbum(@RequestBody params: ApiUpdateAlbumOffsetParams): SimpleResponse {
		albumRepository.findByIdOrNull(params.albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photos = albumPhotoRepository.findByAlbumId(params.albumId)

		val newPhotos = photos.map { it.copy(offset = params.hour * 3600) }

		photoRepository.saveAll(newPhotos)

		return SimpleResponse()
	}
}
