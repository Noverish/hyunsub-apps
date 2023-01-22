package kim.hyunsub.comic.controller

import kim.hyunsub.comic.model.ComicHistorySetParams
import kim.hyunsub.comic.repository.ComicEpisodeRepository
import kim.hyunsub.comic.repository.ComicHistoryRepository
import kim.hyunsub.comic.repository.entity.ComicEpisodeId
import kim.hyunsub.comic.repository.entity.ComicHistory
import kim.hyunsub.comic.repository.entity.ComicHistoryId
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class ComicHistoryController(
	private val comicHistoryRepository: ComicHistoryRepository,
	private val comicEpisodeRepository: ComicEpisodeRepository,
) {
	private val log = KotlinLogging.logger { }

	@PutMapping("/comics/{comicId}/episodes/{order}/history")
	fun setComicHistory(
		userAuth: UserAuth,
		@PathVariable comicId: String,
		@PathVariable order: Int,
		@RequestBody params: ComicHistorySetParams,
	): SimpleResponse {
		val userId = userAuth.idNo
		log.debug { "[Comic History Set] userId=$userId, comicId=$comicId, order=$order, params=$params" }
		comicEpisodeRepository.findByIdOrNull(ComicEpisodeId(comicId, order))
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val history = comicHistoryRepository.findByIdOrNull(ComicHistoryId(userId, comicId, order))
			?: ComicHistory(
				userId = userId,
				comicId = comicId,
				order = order,
			)

		val newHistory = history.copy(page = params.page, date = LocalDateTime.now())
		comicHistoryRepository.save(newHistory)

		return SimpleResponse()
	}
}
