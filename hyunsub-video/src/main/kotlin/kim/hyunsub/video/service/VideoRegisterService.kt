package kim.hyunsub.video.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.VideoRegisterParams
import kim.hyunsub.video.model.VideoRegisterResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

@Service
class VideoRegisterService(
	private val randomGenerator: RandomGenerator,
	private val apiCaller: ApiCaller,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoRepository: VideoRepository,
	private val videoMetadataService: VideoMetadataService,
) {
	companion object : Log

	fun registerVideo(params: VideoRegisterParams): VideoRegisterResult {
		log.debug("registerVideo: params={}", params)

		val videoStat = apiCaller.stat(params.videoPath)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_FILE)
		log.debug("registerVideo: videoStat={}", videoStat)

		val folderPath = Path(params.outputPath).parent.toString()
		log.debug("registerVideo: dirPath={}", folderPath)
		apiCaller.mkdir(folderPath)
		apiCaller.rename(params.videoPath, params.outputPath)

		// 파일 이동 확인
		apiCaller.stat(params.outputPath) ?: throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)

		val videoPath = params.outputPath
		val videoDate = videoStat.mDate
		val videoName = Path(videoPath).nameWithoutExtension
		val thumbnailPath = apiCaller.videoThumbnail(VideoThumbnailParams(input = videoPath)).result

		val videoEntry =
			if (params.videoEntryId != null) {
				videoEntryRepository.findByIdOrNull(params.videoEntryId)
					?: throw ErrorCodeException(ErrorCode.NO_SUCH_VIDEO_ENTRY)
			} else {
				VideoEntry(
					id = randomGenerator.generateRandomString(6),
					name = videoName,
					thumbnail = thumbnailPath,
					category = params.category,
					videoGroupId = params.videoGroupId,
					regDt = videoDate,
				).apply {
					videoEntryRepository.save(this)
				}
			}
		log.debug("registerVideo: videoEntry={}", videoEntry)

		val video = Video(
			id = randomGenerator.generateRandomString(6),
			path = videoPath,
			thumbnail = thumbnailPath,
			regDt = videoDate,
			videoEntryId = videoEntry.id,
			videoSeason = params.videoSeason,
		)
		log.debug("registerVideo: video={}", video)
		videoRepository.save(video)

		val metadata = videoMetadataService.scanAndSave(video.id)
		log.debug("registerVideo: metadata={}", video)

		return VideoRegisterResult(
			videoEntry = videoEntry,
			video = video,
			metadata = metadata,
		)
	}
}
