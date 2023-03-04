package kim.hyunsub.comic.controller

import kim.hyunsub.comic.config.ComicConstants
import kim.hyunsub.comic.model.ApiComicDetail
import kim.hyunsub.comic.model.ApiComicEpisodeDetail
import kim.hyunsub.comic.model.ApiComicPreview
import kim.hyunsub.comic.repository.ComicEpisodeRepository
import kim.hyunsub.comic.repository.ComicHistoryRepository
import kim.hyunsub.comic.repository.ComicRepository
import kim.hyunsub.comic.repository.entity.ComicEpisodeId
import kim.hyunsub.comic.repository.entity.ComicHistoryId
import kim.hyunsub.comic.service.ApiModelConverter
import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/v1/comics")
class ComicListController(
	private val comicRepository: ComicRepository,
	private val comicEpisodeRepository: ComicEpisodeRepository,
	private val comicHistoryRepository: ComicHistoryRepository,
	private val apiModelConverter: ApiModelConverter,
	private val apiCaller: ApiCaller,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun comicList(): List<ApiComicPreview> {
		log.debug { "[Comic List]" }
		return comicRepository.findAll()
			.map { apiModelConverter.convert(it) }
	}

	@GetMapping("/{comicId}")
	fun comicDetail(
		userAuth: UserAuth,
		@PathVariable comicId: String,
	): ApiComicDetail {
		val userId = userAuth.idNo
		log.debug { "[Comic Detail] userId=$userId, comicId=$comicId" }
		val comic = comicRepository.findByIdOrNull(comicId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val episodes = comicEpisodeRepository.findByComicId(comicId)

		val histories = comicHistoryRepository.findByUserIdAndComicId(userId, comicId)

		return apiModelConverter.convert(comic, episodes, histories)
	}

	@GetMapping("/{comicId}/episodes/{order}")
	fun comicEpisodeDetail(
		userAuth: UserAuth,
		@PathVariable comicId: String,
		@PathVariable order: Int,
	): ApiComicEpisodeDetail {
		val userId = userAuth.idNo
		log.debug { "[Comic Episode Detail] userId=$userId, comicId=$comicId, order=$order" }
		val comic = comicRepository.findByIdOrNull(comicId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val episode = comicEpisodeRepository.findByIdOrNull(ComicEpisodeId(comicId, order))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val folderPath = Path(ComicConstants.BASE_PATH, comic.title, episode.title).toString()
		val images = apiCaller.readdir(folderPath)

		val history = comicHistoryRepository.findByIdOrNull(ComicHistoryId(userId, comicId, order))?.page

		val episodeNum = comicEpisodeRepository.countByComicId(comicId)
		val hasNextEpisode = order < episodeNum - 1

		return apiModelConverter.convert(comic, episode, images, hasNextEpisode, history)
	}

	@Authorized(["admin"])
	@PostMapping("/{comicId}/rescan")
	fun rescan(@PathVariable comicId: String): SimpleResponse {
		log.debug { "[Comic Rescan] comicId=$comicId" }
		val comic = comicRepository.findByIdOrNull(comicId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		val episodes = comicEpisodeRepository.findByComicId(comicId)

		for (episode in episodes) {
			val episodePath = Path(ComicConstants.BASE_PATH, comic.title, episode.title).toString()
			val length = apiCaller.readdir(episodePath).size

			if (length != episode.length) {
				val newEpisode = episode.copy(length = length)
				comicEpisodeRepository.save(newEpisode)
			}
		}

		return SimpleResponse()
	}
}