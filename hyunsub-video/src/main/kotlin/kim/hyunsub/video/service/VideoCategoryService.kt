package kim.hyunsub.video.service

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.repository.VideoCategoryRepository
import kim.hyunsub.video.repository.entity.VideoCategory
import org.springframework.stereotype.Service

@Service
class VideoCategoryService(
	private val videoCategoryRepository: VideoCategoryRepository,
) {
	fun getAvailableCategories(user: UserAuth): List<VideoCategory> {
		return videoCategoryRepository.findAll()
			.filter { it.authority.isEmpty() || user.names.contains(it.authority) }
	}
}
