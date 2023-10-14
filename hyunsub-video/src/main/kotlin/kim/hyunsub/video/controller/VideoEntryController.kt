package kim.hyunsub.video.controller

import kim.hyunsub.common.model.RestApiPageResult
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.VideoSort
import kim.hyunsub.video.model.api.RestApiVideoEntry
import kim.hyunsub.video.model.api.RestApiVideoEntryDetail
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.model.dto.VideoEntryCreateParams
import kim.hyunsub.video.model.dto.VideoEntryDeleteResult
import kim.hyunsub.video.model.dto.VideoEntryUpdateParams
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.service.VideoCategoryService
import kim.hyunsub.video.service.VideoEntryService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/entries")
class VideoEntryController(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
	private val videoEntryService: VideoEntryService,
) {
	private val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(
		user: UserAuth,
		@RequestParam category: String,
		@RequestParam seed: Int?,
		@RequestParam(required = false, defaultValue = "0") p: Int,
		@RequestParam(required = false, defaultValue = "48") ps: Int,
		@RequestParam(required = false, defaultValue = "RANDOM") sort: VideoSort,
	): RestApiPageResult<RestApiVideoEntry> {
		val availableCategories = videoCategoryService.getAvailableCategories(user)
		if (availableCategories.none { it.name == category }) {
			return RestApiPageResult.empty()
		}

		val total = videoEntryRepository.countByCategory(category)

		val randomSeed = seed ?: System.currentTimeMillis().toInt()

		val sorted = when (sort) {
			VideoSort.RANDOM -> videoEntryRepository.findByCategoryOrderByRand(category, randomSeed, PageRequest.of(p, ps))
			VideoSort.ABC -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.ASC, "name"))
			VideoSort.ZYX -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.DESC, "name"))
			VideoSort.OLD -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.ASC, "regDt"))
			VideoSort.NEW -> videoEntryRepository.findByCategory(category, PageRequest.of(p, ps, Sort.Direction.DESC, "regDt"))
		}

		return RestApiPageResult(
			total = total,
			page = p,
			pageSize = ps,
			data = sorted.map { it.toApi() },
		)
	}

	@GetMapping("/{entryId}")
	fun get(
		user: UserAuth,
		@PathVariable entryId: String,
	): RestApiVideoEntry {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		return entry.toApi()
	}

	@GetMapping("/{entryId}/detail")
	fun detail(
		user: UserAuth,
		@PathVariable entryId: String,
		@RequestParam(required = false) videoId: String?,
	): RestApiVideoEntryDetail {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		if (!videoEntryService.userHasAuthority(user, entry)) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		return videoEntryService.load(entry, videoId, user.idNo)
	}

	@Authorized(["admin"])
	@PostMapping("")
	fun create(@RequestBody params: VideoEntryCreateParams): RestApiVideoEntry {
		return videoEntryService.create(params).toApi()
	}

	@Authorized(["admin"])
	@PutMapping("/{entryId}")
	fun update(
		@PathVariable entryId: String,
		@RequestBody params: VideoEntryUpdateParams,
	): RestApiVideoEntry {
		log.debug { "[VideoEntryUpdate] entryId=$entryId, params=$params" }
		return videoEntryService.update(entryId, params).toApi()
	}

	@Authorized(["admin"])
	@DeleteMapping("/{entryId}")
	fun delete(@PathVariable entryId: String): VideoEntryDeleteResult {
		return videoEntryService.delete(entryId)
	}
}
