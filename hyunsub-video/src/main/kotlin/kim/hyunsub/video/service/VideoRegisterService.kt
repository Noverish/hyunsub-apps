package kim.hyunsub.video.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.api.model.PhotoConvertParams
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.util.isNotEmpty
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.VideoRegisterParams
import kim.hyunsub.video.model.VideoRegisterResult
import kim.hyunsub.video.repository.VideoCategoryRepository
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoGroup
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.net.URL
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

@Service
class VideoRegisterService(
	private val randomGenerator: RandomGenerator,
	private val apiCaller: ApiCaller,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoRepository: VideoRepository,
	private val videoMetadataService: VideoMetadataService,
	private val videoCategoryRepository: VideoCategoryRepository,
	private val videoGroupRepository: VideoGroupRepository,
	private val fileUrlConverter: FileUrlConverter,
) {
	companion object : Log

	fun registerVideo(params: VideoRegisterParams): VideoRegisterResult {
		log.info("[Register Video] params={}", params)

		// Category 확인
		val category = videoCategoryRepository.findByName(params.category)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such category: ${params.category}")
		log.info("[Register Video] category={}", category)

		// 비디오 파일이 존재하는지 확인
		val videoStat = apiCaller.stat(params.videoPath)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_FILE)
		log.info("[Register Video] videoStat={}", videoStat)

		// 필요하면 비디오 파일 이동
		if (params.videoPath != params.outputPath) {
			val folderPath = Path(params.outputPath).parent.toString()
			log.info("[Register Video] folderPath={}", folderPath)
			apiCaller.mkdir(folderPath)
			apiCaller.rename(params.videoPath, params.outputPath)

			// 파일 이동 확인
			apiCaller.stat(params.outputPath)
				?: throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)
		}

		val videoPath = params.outputPath
		val videoDate = videoStat.mDate
		val videoName = Path(videoPath).nameWithoutExtension

		// 썸네일 생성 또는 다운로드
		val thumbnailPath = if (params.thumbnailUrl.isNotEmpty()) {
			downloadThumbnailFromWeb(videoPath, params.thumbnailUrl)
		} else {
			generateThumbnail(videoPath)
		}

		// Group 확인 또는 생성
		val group = if (params.videoGroupId.isNotEmpty()) {
			videoGroupRepository.findByIdOrNull(params.videoGroupId)
				?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such group: ${params.videoGroupId}")
		} else if (params.newGroupName.isNotEmpty()) {
			generateGroup(params.newGroupName, category.id)
		} else {
			null
		}
		log.info("[Register Video] group={}", group)

		val videoEntry =
			if (params.videoEntryId.isNotEmpty()) {
				videoEntryRepository.findByIdOrNull(params.videoEntryId)
					?: throw ErrorCodeException(ErrorCode.NO_SUCH_VIDEO_ENTRY)
			} else {
				VideoEntry(
					id = VideoEntry.generateId(videoEntryRepository, randomGenerator),
					name = videoName,
					thumbnail = thumbnailPath,
					category = category.name,
					videoGroupId = group?.id,
					regDt = videoDate,
				).apply {
					videoEntryRepository.save(this)
				}
			}
		log.info("[Register Video] videoEntry={}", videoEntry)

		val video = Video(
			id = Video.generateId(videoRepository, randomGenerator),
			path = videoPath,
			thumbnail = thumbnailPath,
			regDt = videoDate,
			videoEntryId = videoEntry.id,
			videoSeason = params.videoSeason,
		)
		log.info("[Register Video] video={}", video)
		videoRepository.save(video)

		val metadata = videoMetadataService.scanAndSave(video.id)
		log.info("[Register Video] metadata={}", video)

		return VideoRegisterResult(
			videoEntry = videoEntry,
			video = video,
			metadata = metadata,
		)
	}

	/**
	 * @return thumbnailPath
	 */
	private fun generateThumbnail(videoPath: String): String {
		val videoExt = Path(videoPath).extension
		val thumbnailPath = videoPath.replace(Regex("$videoExt$"), "jpg")

		if (apiCaller.stat(thumbnailPath) == null) {
			log.info("[Register Video] generate thumbnail: {}", thumbnailPath)
			apiCaller.videoThumbnail(
				VideoThumbnailParams(input = videoPath, output = thumbnailPath)
			)
		} else {
			log.info("[Register Video] thumbnail is already exist: {}", thumbnailPath)
		}

		return thumbnailPath
	}

	/**
	 * @return thumbnailPath
	 */
	private fun downloadThumbnailFromWeb(videoPath: String, url: String): String {
		log.info("[Register Video] Download thumbnail from web: url={}", url)
		val videoExt = Path(videoPath).extension
		val thumbnailExt = Path(URL(url).path).extension.ifEmpty { "jpg" }
		val thumbnailOriginalPath = videoPath.replace(Regex("$videoExt$"), "original.$thumbnailExt")
		log.info("[Register Video] Download thumbnail from web: thumbnailOriginalPath={}", thumbnailOriginalPath)

		val nonce = apiCaller.uploadByUrl(url).nonce
		val noncePath = fileUrlConverter.getNoncePath(nonce)
		log.info("[Register Video] Download thumbnail from web: noncePath={}", noncePath)

		apiCaller.rename(noncePath, thumbnailOriginalPath)

		val thumbnailPath = videoPath.replace(Regex("$videoExt$"), thumbnailExt)
		log.info("[Register Video] Download thumbnail from web: thumbnailPath={}", thumbnailPath)

		apiCaller.imageConvert(
			PhotoConvertParams(
				input = thumbnailOriginalPath,
				output = thumbnailPath,
				resize = "512x512>",
				quality = 60,
			)
		)

		return thumbnailPath
	}

	private fun generateGroup(newGroupName: String, categoryId: Int): VideoGroup {
		val existGroup = videoGroupRepository.findByName(newGroupName)
		if (existGroup != null) {
			return existGroup
		}

		val group = VideoGroup(
			id = VideoGroup.generateId(videoGroupRepository, randomGenerator),
			name = newGroupName,
			categoryId = categoryId,
		)
		videoGroupRepository.save(group)
		return group
	}
}
