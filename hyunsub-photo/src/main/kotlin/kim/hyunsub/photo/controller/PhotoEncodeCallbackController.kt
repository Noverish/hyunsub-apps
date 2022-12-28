package kim.hyunsub.photo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.repository.PhotoMetadataRepository
import kim.hyunsub.photo.repository.PhotoRepository
import kim.hyunsub.photo.repository.entity.PhotoMetadata
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/encode/callback")
class PhotoEncodeCallbackController(
	private val apiCaller: ApiCaller,
	private val photoRepository: PhotoRepository,
	private val photoMetadataRepository: PhotoMetadataRepository,
	private val mapper: ObjectMapper,
) {
	companion object : Log

	@GetMapping("")
	fun callback(photoId: Int): SimpleResponse {
		log.debug("[Encode Callback] photoId={}", photoId)

		val photo = photoRepository.findByIdOrNull(photoId) ?: run {
			log.error("[Encode Callback] Wrong photo id: {}", photoId)
			return SimpleResponse()
		}
		log.debug("[Encode Callback] photo={}", photo)

		val metadataStr = apiCaller.exif(photo.path)
		log.debug("[Encode Callback] metadataStr={}", metadataStr)
		val metadataNode = mapper.readTree(metadataStr)
		val size = metadataNode[0]["FileSize"].asInt()
		val metadata = PhotoMetadata(
			path = photo.path,
			data = metadataStr,
			date = LocalDateTime.now()
		)

		photoMetadataRepository.saveAndFlush(metadata)

		photo.copy(size = size).let {
			photoRepository.saveAndFlush(it)
		}

		return SimpleResponse()
	}
}
