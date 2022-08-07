package kim.hyunsub.video.service

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideoEntryDetail
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class VideoEntryService(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
	private val videoRepository: VideoRepository,
	private val restModelConverter: RestModelConverter,
	private val videoGroupService: VideoGroupService,
	private val videoService: VideoService,
) {
	fun userHasAuthority(user: UserAuth, entryId: String): Boolean {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		return userHasAuthority(user, entry)
	}

	fun userHasAuthority(user: UserAuth, entry: VideoEntry): Boolean {
		val availableCategories = videoCategoryService.getAvailableCategories(user)
		return availableCategories.any { it.name == entry.category }
	}

	fun load(entry: VideoEntry, videoId: String?): RestVideoEntryDetail {
		val videos = videoRepository.findByVideoEntryId(entry.id)
		if (videos.isEmpty()) {
			throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)
		}

		val video = chooseVideo(videos, videoId)

		val group = entry.videoGroupId?.let { videoGroupService.loadVideoGroup(it) }

		val episodes =
			if (videos.size > 1) {
				videos.groupBy { it.videoSeason ?: "" }
					.mapValues { (_, v) -> v.map { restModelConverter.convertToEpisode(it) }.sortedBy { it.title } }
			} else {
				null
			}

		return RestVideoEntryDetail(
			video = videoService.loadVideo(video),
			episodes = episodes,
			group = group,
		)
	}

	private fun chooseVideo(videos: List<Video>, videoId: String?): Video {
		if (videoId != null) {
			return videos.firstOrNull { it.id == videoId }
				?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		val season = videos.mapNotNull { it.videoSeason }.distinct().minOrNull()
		return if (season == null) {
			videos.minBy { it.path }
		} else {
			val episodes = videos.filter { it.videoSeason == season }
			episodes.minBy { it.path }
		}
	}
}
