package kim.hyunsub.photo.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.model.api.ApiRescanPhotoDateParams
import kim.hyunsub.photo.model.api.ApiRescanPhotoDateResult
import kim.hyunsub.photo.repository.AlbumPhotoRepository
import kim.hyunsub.photo.repository.AlbumV2Repository
import kim.hyunsub.photo.repository.PhotoMetadataV2Repository
import kim.hyunsub.photo.repository.PhotoV2Repository
import kim.hyunsub.photo.repository.generateId
import kim.hyunsub.photo.service.PhotoUpdateService
import kim.hyunsub.photo.util.PhotoDateParser
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/admin")
class PhotoAdminController(
	private val albumRepository: AlbumV2Repository,
	private val albumPhotoRepository: AlbumPhotoRepository,
	private val photoMetadataRepository: PhotoMetadataV2Repository,
	private val photoRepository: PhotoV2Repository,
	private val photoUpdateService: PhotoUpdateService,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	@PostMapping("/rescan-photo-date")
	fun rescanPhotoDate(@RequestBody params: ApiRescanPhotoDateParams): ApiRescanPhotoDateResult {
		val results = mutableListOf<String>()
		val albumId = params.albumId

		albumRepository.findByIdOrNull(albumId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val photos = albumPhotoRepository.findByAlbumId(albumId)

		for (photo in photos) {
			val metadata = photoMetadataRepository.findByIdOrNull(photo.id)
				?: continue

			if (photo.dateType != PhotoDateType.EXIF) {
				continue
			}

			val exif = mapper.readTree(metadata.raw)
			val result = PhotoDateParser.parse(exif, "", System.currentTimeMillis())
			if (result.type != PhotoDateType.EXIF) {
				continue
			}

			if (photo.date == result.date) {
				continue
			}

			val newId = photoRepository.generateId(result.date, photo.hash)
			photoUpdateService.updateId(photo, newId)

			val str = "${photo.id} -> $newId: ${photo.date} -> ${result.date}"
			log.debug { "[Rescan Photo Date] $str" }
			results.add(str)
		}

		return ApiRescanPhotoDateResult(results)
	}
}
