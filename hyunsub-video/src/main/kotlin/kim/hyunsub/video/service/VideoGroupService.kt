package kim.hyunsub.video.service

import kim.hyunsub.video.model.api.ApiVideoGroupDetail
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class VideoGroupService(
	private val videoGroupRepository: VideoGroupRepository,
	private val videoEntryRepository: VideoEntryRepository,
) {
	fun loadVideoGroup(videoGroupId: String): ApiVideoGroupDetail? {
		val videoGroup = videoGroupRepository.findByIdOrNull(videoGroupId) ?: return null
		val entries = videoEntryRepository.findByVideoGroupId(videoGroupId)
			.map { it.toApi() }
			.sortedBy { it.name }
		return ApiVideoGroupDetail(
			name = videoGroup.name,
			entries = entries,
		)
	}
}
