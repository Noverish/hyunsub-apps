package kim.hyunsub.video.controller

import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.RestVideoDetail
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.service.RestModelConverter
import kim.hyunsub.video.service.VideoCategoryService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(authorities = ["service_video"])
@RestController
@RequestMapping("/api/v1/detail")
class VideoDetailController(
	private val videoEntryRepository: VideoEntryRepository,
	private val videoCategoryService: VideoCategoryService,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoRepository: VideoRepository,
	private val restModelConverter: RestModelConverter,
) {
	companion object : Log

	@GetMapping("/{entryId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable entryId: String,
	): RestVideoDetail {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val availableCategories = videoCategoryService.getAvailableCategories(userAuth.authorityNames)
		if (availableCategories.none { it.name == entry.category }) {
			throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		val video = videoRepository.findByVideoEntryIdIn(listOf(entry.id)).firstOrNull()
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val subtitles = videoSubtitleRepository.findByVideoIdIn(listOf(video.id))

		return restModelConverter.convertVideoDetail(entry, video, subtitles)
	}
}
