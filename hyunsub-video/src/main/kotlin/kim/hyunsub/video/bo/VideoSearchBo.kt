package kim.hyunsub.video.bo

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.model.dto.VideoSearchResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.stereotype.Service

@Service
class VideoSearchBo(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
) {
	fun search(user: UserAuth, query: String): VideoSearchResult {
		val categories = videoCategoryService.getAvailableCategories(user).map { it.name }
		if (query.length < 2) {
			throw ErrorCodeException(ErrorCode.SHORT_SEARCH_QUERY)
		}

		val entries = videoEntryRepository.findByNameContaining(query)
			.filter { categories.contains(it.category) }
			.groupBy { it.category }
			.mapValues { entry -> entry.value.map { it.toApi() } }

		return VideoSearchResult(entries)
	}
}
