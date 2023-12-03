package kim.hyunsub.photo.controller.admin

import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.repository.PhotoRepository
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/encode/callback")
class PhotoEncodeCallbackController(
	private val photoRepository: PhotoRepository,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun callback(photoId: String): SimpleResponse {
		log.debug("[Encode Callback] photoId={}", photoId)

		val photo = photoRepository.findByIdOrNull(photoId) ?: run {
			log.error("[Encode Callback] Wrong photo id: {}", photoId)
			return SimpleResponse()
		}
		log.debug("[Encode Callback] photo={}", photo)

		return SimpleResponse()
	}
}
