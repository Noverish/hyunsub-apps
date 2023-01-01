package kim.hyunsub.comic.controller

import kim.hyunsub.comic.model.ComicDetail
import kim.hyunsub.comic.model.ComicEpisodeDetail
import kim.hyunsub.comic.model.ComicEpisodePreview
import kim.hyunsub.comic.model.ComicPreview
import kim.hyunsub.common.api.ApiCaller
import mu.KotlinLogging
import org.springframework.util.Base64Utils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/v1/comics")
class ComicListController(
	private val apiCaller: ApiCaller,
) {
	companion object {
		const val BASE_PATH = "/Comics"
	}
	private val log = KotlinLogging.logger {  }

	@GetMapping("")
	fun comicPreviews(): List<ComicPreview> {
		return apiCaller.readdir(BASE_PATH).map { ComicPreview(it) }
	}

	@GetMapping("/{nameBase64}")
	fun comicDetail(@PathVariable nameBase64: String): ComicDetail {
		val name = String(Base64Utils.decodeFromUrlSafeString(nameBase64))
		log.debug { "[Comic Detail] $name" }
		return apiCaller.readdir(Path(BASE_PATH, name).toString())
			.map { ComicEpisodePreview(it) }
			.let { ComicDetail(it) }
	}

	@GetMapping("/{nameBase64}/episodes/{episodeBase64}")
	fun episodeDetail(
		@PathVariable nameBase64: String,
		@PathVariable episodeBase64: String,
	): ComicEpisodeDetail {
		val name = String(Base64Utils.decodeFromUrlSafeString(nameBase64))
		val episode = String(Base64Utils.decodeFromUrlSafeString(episodeBase64))
		log.debug { "[Episode Detail] $name" }
		return apiCaller.readdir(Path(BASE_PATH, name, episode).toString())
			.let { ComicEpisodeDetail(it) }
	}
}
