package kim.hyunsub.comic.controller

import kim.hyunsub.comic.config.ComicConstants
import kim.hyunsub.comic.model.ComicRegisterParams
import kim.hyunsub.comic.model.ComicScanParams
import kim.hyunsub.comic.model.ComicScanResult
import kim.hyunsub.comic.model.ComicScanResultData
import kim.hyunsub.comic.repository.ComicEpisodeRepository
import kim.hyunsub.comic.repository.ComicRepository
import kim.hyunsub.comic.repository.entity.Comic
import kim.hyunsub.comic.repository.entity.ComicEpisode
import kim.hyunsub.comic.repository.generateId
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.name

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/admin")
class ComicAdminController(
	private val comicRepository: ComicRepository,
	private val comicEpisodeRepository: ComicEpisodeRepository,
	private val fsClient: FsClient,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/register")
	fun register(@RequestBody params: ComicRegisterParams): Comic {
		log.debug { "[Comic Register] params=$params" }

		val comic = Comic(
			id = comicRepository.generateId(),
			title = params.title,
		)
		log.debug { "[Comic Register] comic=$comic" }

		comicRepository.save(comic)

		return comic
	}

	@PostMapping("/scan")
	fun scan(@RequestBody params: ComicScanParams): ComicScanResult {
		log.debug { "[Comic Scan] params=$params" }
		val comic = comicRepository.findByIdOrNull(params.comicId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		// 에피소드 별 사진 갯수 검증
		val lengthChanged = mutableListOf<ComicScanResultData>()
		val episodes = comicEpisodeRepository.findByComicId(params.comicId)
		for (episode in episodes) {
			val episodePath = Path(ComicConstants.BASE_PATH, comic.title, episode.title).toString()
			val length = fsClient.readdir(episodePath).size

			if (length != episode.length) {
				val data = ComicScanResultData(episode.title, episode.length, length)
				lengthChanged.add(data)
				log.debug { "[Comic Scan] length changed: $data" }

				val newEpisode = episode.copy(length = length)
				if (!params.dryRun) {
					comicEpisodeRepository.save(newEpisode)
				}
			}
		}

		// 미등록 에피소드 확인
		val newEpisodes = mutableListOf<ComicEpisode>()
		val comicPath = Path(ComicConstants.BASE_PATH, comic.title).toString()
		val folders = fsClient.readdirDetail(comicPath)
			.filter { it.isDir == true }
			.map { Path(it.path).name }
			.sorted()

		val episodeTitles = episodes.map { it.title }
		val candidates = (folders - episodeTitles.toSet()).sorted()
		for ((i, candidate) in candidates.withIndex()) {
			val candidatePath = Path(comicPath, candidate).toString()
			val length = fsClient.readdir(candidatePath).size

			val episode = ComicEpisode(
				comicId = params.comicId,
				order = episodes.size + i,
				title = candidate,
				length = length,
				regDt = LocalDateTime.now().plusSeconds(i.toLong())
			)
			log.debug { "[Comic Scan] new episode: $episode" }
			newEpisodes.add(episode)
		}

		if (!params.dryRun) {
			comicEpisodeRepository.saveAll(newEpisodes)
		}

		return ComicScanResult(
			lengthChanged = lengthChanged,
			newEpisodes = newEpisodes,
		)
	}
}
