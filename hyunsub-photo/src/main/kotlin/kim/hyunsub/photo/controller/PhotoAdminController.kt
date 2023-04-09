package kim.hyunsub.photo.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.model.PhotoDateType
import kim.hyunsub.photo.model.api.ApiRescanPhotoDateParams
import kim.hyunsub.photo.model.api.ApiUpdatePhotoOffsetParams
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
import java.time.ZoneOffset

@RestController
@RequestMapping("/api/v1/admin")
class PhotoAdminController(
	private val photoMetadataRepository: PhotoMetadataV2Repository,
	private val photoRepository: PhotoV2Repository,
	private val photoUpdateService: PhotoUpdateService,
) {
	private val log = KotlinLogging.logger { }
	private val mapper = jacksonObjectMapper()

	@PostMapping("/rescan-photo-date")
	fun rescanPhotoDate(@RequestBody params: ApiRescanPhotoDateParams): SimpleResponse {
		val photo = photoRepository.findByIdOrNull(params.photoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val metadata = photoMetadataRepository.findByIdOrNull(photo.id)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val exif = mapper.readTree(metadata.raw)

		val result = PhotoDateParser.parse(exif, "", System.currentTimeMillis())
		if (result.type != PhotoDateType.EXIF) {
			return SimpleResponse(false)
		}

		if (result.date == photo.date) {
			return SimpleResponse(false)
		}

		val newId = photoRepository.generateId(result.date, photo.hash)
		log.debug { "[Rescan Photo Date] ${photo.id} -> $newId : ${photo.date} -> ${result.date}" }
		photoUpdateService.updateId(photo, newId)

		val newPhoto = photo.copy(id = newId, dateType = PhotoDateType.EXIF)
		photoRepository.save(newPhoto)

		return SimpleResponse()
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
}
