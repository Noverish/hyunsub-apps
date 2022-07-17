package kim.hyunsub.video.service

import kim.hyunsub.video.repository.VideoCategoryRepository
import kim.hyunsub.video.repository.entity.VideoCategory
import org.springframework.stereotype.Service

@Service
class VideoCategoryService(
	private val videoCategoryRepository: VideoCategoryRepository,
) {
	fun getAvailableCategories(authorities: List<String>): List<VideoCategory> {
		return videoCategoryRepository.findAll()
			.filter { it.authority.isEmpty() || authorities.contains(it.authority) }
	}
}
