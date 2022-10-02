package kim.hyunsub.video.service

import kim.hyunsub.video.model.RestVideoGroup
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class VideoGroupService(
	private val videoGroupRepository: VideoGroupRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val apiModelConverter: ApiModelConverter,
) {
	fun loadVideoGroup(videoGroupId: String): RestVideoGroup? {
		val videoGroup = videoGroupRepository.findByIdOrNull(videoGroupId) ?: return null
		val entries = videoEntryRepository.findByVideoGroupId(videoGroupId)
			.map { apiModelConverter.convert(it) }
			.sortedBy { it.name }
		return RestVideoGroup(
			name = videoGroup.name,
			entries = entries,
		)
	}
}
