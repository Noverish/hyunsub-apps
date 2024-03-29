package kim.hyunsub.video.service

import kim.hyunsub.video.model.VideoSearchResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.VideoCategory
import org.springframework.stereotype.Service

@Service
class VideoSearchService(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoRepository: VideoRepository,
) {
	fun search(categories: List<String>, query: String): VideoSearchResult {
		val entries = videoEntryRepository.findByNameContaining(query)
			.filter { categories.contains(it.category) }

		return VideoSearchResult(
			entries = entries
		)
	}
}
