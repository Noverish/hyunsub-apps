package kim.hyunsub.comic.controller

import kim.hyunsub.comic.config.ComicConstants
import kim.hyunsub.comic.model.ComicRegisterParams
import kim.hyunsub.comic.model.ComicRegisterResult
import kim.hyunsub.comic.repository.ComicEpisodeRepository
import kim.hyunsub.comic.repository.ComicRepository
import kim.hyunsub.comic.repository.entity.Comic
import kim.hyunsub.comic.repository.entity.ComicEpisode
import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.util.generateRandomString
import kim.hyunsub.common.web.annotation.Authorized
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import kotlin.io.path.Path

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/register")
class ComicRegisterController(
	private val comicRepository: ComicRepository,
	private val comicEpisodeRepository: ComicEpisodeRepository,
	private val fsClient: FsClient,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("")
	fun register(@RequestBody params: ComicRegisterParams): ComicRegisterResult {
		log.debug { "[Comic Register] params=$params" }

		val comicPath = Path(ComicConstants.BASE_PATH, params.title).toString()
		val episodeTitles = fsClient.readdir(comicPath).sorted()

		val comicId = generateRandomString(6)

		val comic = Comic(
			id = comicId,
			title = params.title,
		)
		log.debug { "[Comic Register] comic=$comic" }
		comicRepository.save(comic)

		val episodes = mutableListOf<ComicEpisode>()
		for ((i, episodeTitle) in episodeTitles.withIndex()) {
			val episodePath = Path(comicPath, episodeTitle).toString()
			val length = fsClient.readdir(episodePath).size

			val episode = ComicEpisode(
				comicId = comicId,
				order = i,
				title = episodeTitle,
				length = length,
				regDt = LocalDateTime.now().plusSeconds(i.toLong())
			)
			log.debug { "[Comic Register] episode=$episode" }
			comicEpisodeRepository.save(episode)
			episodes.add(episode)
		}

		return ComicRegisterResult(
			comic = comic,
			episodes = episodes,
		)
	}
}
