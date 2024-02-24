package kim.hyunsub.photo.controller.admin

import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.photo.repository.mapper.PhotoMapper
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/encode/callback")
class PhotoEncodeCallbackController(
	private val photoMapper: PhotoMapper,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun callback(photoId: String): SimpleResponse {
		log.debug("[Encode Callback] photoId={}", photoId)

		val photo = photoMapper.selectOne(photoId) ?: run {
			log.error("[Encode Callback] Wrong photo id: {}", photoId)
			return SimpleResponse()
		}
		log.debug("[Encode Callback] photo={}", photo)

		return SimpleResponse()
	}
}
