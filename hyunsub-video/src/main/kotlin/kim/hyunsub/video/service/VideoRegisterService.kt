package kim.hyunsub.video.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.api.model.ApiPhotoConvertParams
import kim.hyunsub.common.api.model.VideoThumbnailParams
import kim.hyunsub.common.fs.FsClient
import kim.hyunsub.common.fs.FsVideoClient
import kim.hyunsub.common.fs.exist
import kim.hyunsub.common.fs.statOrNull
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.common.util.isNotEmpty
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.dto.VideoRegisterParams
import kim.hyunsub.video.model.dto.VideoRegisterResult
import kim.hyunsub.video.repository.VideoCategoryRepository
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoGroup
import kim.hyunsub.video.repository.generateId
import mu.KotlinLogging
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
	private val fsVideoClient: FsVideoClient,
	private val fsClient: FsClient,
) {
	val log = KotlinLogging.logger { }

	fun registerVideo(params: VideoRegisterParams): VideoRegisterResult {
		log.info("[Register Video] params={}", params)

		// Entry 확인
		val entryFromParam =
			if (params.videoEntryId.isNotEmpty()) {
				videoEntryRepository.findByIdOrNull(params.videoEntryId)
					.apply { log.info("[Register Video] entry={}", this) }
					?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No such entry: ${params.videoEntryId}")
			} else {
				null
			}

		// Category 확인
		val categoryFromParam =
			if (params.category.isNotEmpty()) {
				videoCategoryRepository.findByName(params.category)
					.apply { log.info("[Register Video] category={}", this) }
					?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No such category: ${params.category}")
			} else {
				null
			}

		// Group 확인
		val groupFromParam =
			if (params.videoGroupId.isNotEmpty()) {
				videoGroupRepository.findByIdOrNull(params.videoGroupId)
					.apply { log.info("[Register Video] group={}", this) }
					?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "No such group: ${params.videoGroupId}")
			} else {
				null
			}

		// 비디오 파일이 존재하는지 확인
		val videoStat = fsClient.statOrNull(params.videoPath)
			?: throw ErrorCodeException(ErrorCode.NO_SUCH_FILE)
		log.info("[Register Video] videoStat={}", videoStat)

		// 필요하면 비디오 파일 이동
		if (params.videoPath != params.outputPath) {
			val folderPath = Path(params.outputPath).parent.toString()
			log.info("[Register Video] folderPath={}", folderPath)
			apiCaller.mkdir(folderPath)
			apiCaller.rename(params.videoPath, params.outputPath)

			// 파일 이동 확인
			if (!fsClient.exist(params.outputPath)) {
				throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)
			}
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

		val videoEntry = entryFromParam
			?: run {
				if (categoryFromParam == null) {
					throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "category is null")
				}

				// Group 확인 또는 생성
				val group = if (params.newGroupName.isNotEmpty() && groupFromParam == null) {
					generateGroup(params.newGroupName, categoryFromParam.id)
				} else {
					groupFromParam
				}
				log.info("[Register Video] group={}", group)

				VideoEntry(
					id = videoEntryRepository.generateId(),
					name = videoName,
					thumbnail = thumbnailPath,
					category = categoryFromParam.name,
					videoGroupId = group?.id,
					regDt = videoDate,
				).apply {
					videoEntryRepository.save(this)
				}
			}
		log.info("[Register Video] videoEntry={}", videoEntry)

		val video = Video(
			id = videoRepository.generateId(),
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
	fun generateThumbnail(videoPath: String): String {
		val videoExt = Path(videoPath).extension
		val thumbnailPath = videoPath.replace(Regex("$videoExt$"), "jpg")

		if (fsClient.exist(thumbnailPath)) {
			log.info("[Register Video] thumbnail is already exist: {}", thumbnailPath)
		} else {
			log.info("[Register Video] generate thumbnail: {}", thumbnailPath)
			fsVideoClient.videoThumbnail(
				VideoThumbnailParams(input = videoPath, output = thumbnailPath)
			)
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
		val noncePath = FileUrlConverter.noncePath(nonce)
		log.info("[Register Video] Download thumbnail from web: noncePath={}", noncePath)

		apiCaller.rename(noncePath, thumbnailOriginalPath)

		val thumbnailPath = videoPath.replace(Regex("$videoExt$"), thumbnailExt)
		log.info("[Register Video] Download thumbnail from web: thumbnailPath={}", thumbnailPath)

		apiCaller.imageConvert(
			ApiPhotoConvertParams(
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
