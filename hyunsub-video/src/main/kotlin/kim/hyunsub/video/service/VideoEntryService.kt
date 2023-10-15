package kim.hyunsub.video.service

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.FsImageClient
import kim.hyunsub.common.fs.client.FsUploadClient
import kim.hyunsub.common.fs.client.mkdir
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.fs.client.url
import kim.hyunsub.common.fs.model.ImageConvertParams
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.video.model.api.RestApiVideoEntryDetail
import kim.hyunsub.video.model.api.RestApiVideoSeason
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.model.dto.VideoEntryCreateParams
import kim.hyunsub.video.model.dto.VideoEntryDeleteResult
import kim.hyunsub.video.model.dto.VideoEntryUpdateParams
import kim.hyunsub.video.repository.VideoCategoryRepository
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoGroupRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.generateId
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.net.URL
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.extension

@Service
class VideoEntryService(
	private val videoCategoryService: VideoCategoryService,
	private val videoRepository: VideoRepository,
	private val videoGroupService: VideoGroupService,
	private val videoService: VideoService,
	private val videoCategoryRepository: VideoCategoryRepository,
	private val videoGroupRepository: VideoGroupRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val fsClient: FsClient,
	private val fsImageClient: FsImageClient,
	private val fsUploadClient: FsUploadClient,
) {
	private val log = KotlinLogging.logger { }

	fun userHasAuthority(user: UserAuth, entry: VideoEntry): Boolean {
		val availableCategories = videoCategoryService.getAvailableCategories(user)
		return availableCategories.any { it.name == entry.category }
	}

	fun load(entry: VideoEntry, videoId: String?, userId: String): RestApiVideoEntryDetail {
		val videos = videoRepository.findByEntryId(entry.id)
		if (videos.isEmpty()) {
			throw ErrorCodeException(ErrorCode.EMPTY_VIDEO_ENTRY)
		}

		val video = chooseVideo(videos, videoId)

		val episodes = videoRepository.selectEpisode(entry.id, userId)

		val seasons = if (episodes.size > 1) {
			episodes.groupBy { it.season }
				.map { (key, value) -> RestApiVideoSeason(key, value) }
		} else {
			null
		}

		val group = entry.videoGroupId?.let { videoGroupService.loadVideoGroup(it) }

		val category = videoCategoryRepository.findByName(entry.category)
			?: throw ErrorCodeException(ErrorCode.INTERNAL_SERVER_ERROR)

		return RestApiVideoEntryDetail(
			category = category.toDto(),
			entry = entry.toApi(),
			video = videoService.loadVideo(userId, video),
			seasons = seasons,
			group = group,
		)
	}

	private fun chooseVideo(videos: List<Video>, videoId: String?): Video {
		if (videoId != null) {
			return videos.firstOrNull { it.id == videoId }
				?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		}

		val season = videos.mapNotNull { it.season }.distinct().minOrNull()
		return if (season == null) {
			videos.minBy { it.path }
		} else {
			val episodes = videos.filter { it.season == season }
			episodes.minBy { it.path }
		}
	}

	fun create(params: VideoEntryCreateParams): VideoEntry {
		params.videoGroupId?.let {
			videoGroupRepository.findByIdOrNull(it)
				?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such videoGroupId")
		}

		val category = videoCategoryRepository.findByName(params.category)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such category: ${params.category}")

		val folderPath = Path(category.path, params.name).toString()

		fsClient.mkdir(folderPath)

		val thumbnail = params.thumbnailUrl?.let {
			val thumbnailExt = Path(URL(it).path).extension.ifEmpty { "jpg" }
			val thumbnailOriginalPath = "$folderPath/thumbnail.original.$thumbnailExt"

			val nonce = fsUploadClient.url(it).nonce
			val noncePath = FsPathConverter.noncePath(nonce)

			fsClient.rename(noncePath, thumbnailOriginalPath)

			val thumbnailPath = "$folderPath/thumbnail.jpg"

			fsImageClient.convert(
				ImageConvertParams(
					input = thumbnailOriginalPath,
					output = thumbnailPath,
					resize = "512x512>",
					quality = 60,
				)
			)

			thumbnailPath
		}

		val entry = VideoEntry(
			id = videoEntryRepository.generateId(),
			name = params.name,
			thumbnail = thumbnail,
			category = params.category,
			regDt = LocalDateTime.now(),
			videoGroupId = params.videoGroupId,
		)

		videoEntryRepository.save(entry)

		return entry
	}

	fun update(entryId: String, params: VideoEntryUpdateParams): VideoEntry {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such entryId")
		log.debug { "[VideoEntryUpdate] entry=$entry" }

		val newEntry = entry.copy(
			name = params.name ?: entry.name,
		)

		videoEntryRepository.save(newEntry)

		return newEntry
	}

	fun delete(entryId: String): VideoEntryDeleteResult {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such entryId")

		val videos = videoRepository.findByEntryId(entryId)
			.map { videoService.delete(it) }

		videoEntryRepository.delete(entry)

		return VideoEntryDeleteResult(entry, videos)
	}
}
