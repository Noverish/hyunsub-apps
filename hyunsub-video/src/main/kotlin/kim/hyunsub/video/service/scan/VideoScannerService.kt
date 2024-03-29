package kim.hyunsub.video.service.scan

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.RestScanParams
import kim.hyunsub.video.model.ScanResult
import kim.hyunsub.video.model.VideoRegisterParams
import kim.hyunsub.video.model.VideoRegisterResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoSubtitle
import kim.hyunsub.video.service.VideoMetadataService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

@Service
class VideoScannerService(
	private val randomGenerator: RandomGenerator,
	private val apiCaller: ApiCaller,
	private val videoGroupRepository: VideoGroupRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoRepository: VideoRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoMetadataService: VideoMetadataService,
) {
	companion object : Log

	@Transactional
	fun scan(params: RestScanParams): ScanResult {
		log.debug("Video Scan Params: {}", params)
		val files = apiCaller.walkDetail(params.path)

		val scanner: VideoScanner = when (params.type) {
			1 -> VideoType1Scanner(randomGenerator, params.category, files)
			2 -> VideoType2Scanner(randomGenerator, params.category, files)
			3 -> VideoType3Scanner(randomGenerator, params.category, files)
			4 -> VideoType4Scanner(randomGenerator, params.category, files)
			else -> throw IllegalArgumentException("No such VideoScanner type: ${params.type}")
		}

		val result = scanner.scan(params.path)

		result.videoGroups.forEach { log.debug("Video Scan Result: {}", it) }
		result.videoEntries.forEach { log.debug("Video Scan Result: {}", it) }
		result.videos.forEach { log.debug("Video Scan Result: {}", it) }
		result.subtitles.forEach { log.debug("Video Scan Result: {}", it) }

		deleteData(params.category)
		insertData(result)

		return result
	}

	private fun deleteData(category: String) {
		val videoEntries = videoEntryRepository.findByCategory(category)
		val videoEntryNum = videoEntryRepository.deleteByCategory(category)
		log.debug("[Video Scan Delete] # of VideoEntries: {}", videoEntryNum)

		val videoGroupIds = videoEntries.mapNotNull { it.videoGroupId }
		val videoGroupNum = videoGroupRepository.deleteByIdIn(videoGroupIds)
		log.debug("[Video Scan Delete] # of videoGroups: {}", videoGroupNum)

		val videoEntryIds = videoEntries.map { it.id }
		val videos = videoRepository.findByVideoEntryIdIn(videoEntryIds)
		val videoNum = videoRepository.deleteByVideoEntryIdIn(videoEntryIds)
		log.debug("[Video Scan Delete] # of videos: {}", videoNum)

		val videoIds = videos.map { it.id }
		val videoSubtitleNum = videoSubtitleRepository.deleteByVideoIdIn(videoIds)
		log.debug("[Video Scan Delete] # of videoSubtitles: {}", videoSubtitleNum)
	}

	private fun insertData(result: ScanResult) {
		videoGroupRepository.saveAll(result.videoGroups)
		videoEntryRepository.saveAll(result.videoEntries)
		videoRepository.saveAll(result.videos)
		videoSubtitleRepository.saveAll(result.subtitles)
	}
}
