package kim.hyunsub.video.service.scan

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.model.RestScanParams
import kim.hyunsub.video.model.ScanResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.service.scan.VideoScanner
import kim.hyunsub.video.service.scan.VideoType1Scanner
import org.springframework.stereotype.Service

@Service
class VideoScannerService(
	private val randomGenerator: RandomGenerator,
	private val apiCaller: ApiCaller,
	private val videoGroupRepository: VideoGroupRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoRepository: VideoRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
) {
	companion object : Log

	fun scan(params: RestScanParams): ScanResult {
		log.debug("Video Scan Params: {}", params)
		val files = apiCaller.walk2(params.path)

		val scanner: VideoScanner = when (params.type) {
			1 -> VideoType1Scanner(randomGenerator, params.category, files)
			else -> throw IllegalArgumentException("No such VideoScanner type: ${params.type}")
		}

		val result = scanner.scan(params.path)
		log.debug("Video Scan Result: {}", result)
		return result
	}
}
