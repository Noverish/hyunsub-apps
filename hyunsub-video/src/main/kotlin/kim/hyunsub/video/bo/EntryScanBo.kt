package kim.hyunsub.video.bo

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.model.FileStat
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.dto.EntryScanResult
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoSubtitle
import kim.hyunsub.video.repository.generateId
import kim.hyunsub.video.service.VideoMetadataService
import kim.hyunsub.video.service.VideoRegisterService
import kim.hyunsub.video.util.isSubtitle
import kim.hyunsub.video.util.isVideo
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

@Service
class EntryScanBo(
	private val fsClient: FsClient,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoRepository: VideoRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoRegisterService: VideoRegisterService,
	private val videoMetadataService: VideoMetadataService,
	private val videoMetadataRepository: VideoMetadataRepository,
) {
	private val log = KotlinLogging.logger { }

	fun scan(entryId: String): List<EntryScanResult> {
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val path = entry.thumbnail?.let { Path(it).parent.toString() }
			?: throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "thumbnail is not exist")

		val list = fsClient.readdirDetail(path)

		val result = mutableListOf<EntryScanResult>()

		val files = list.filter { it.isDir != true }

		result.addAll(scan(entryId, files, null))

		list.filter { it.isDir == true }
			.forEach { result.addAll(scanDir(entryId, it)) }

		return result
	}

	fun scanDir(entryId: String, stat: FileStat): List<EntryScanResult> {
		val season = Path(stat.path).name
		val files = fsClient.readdirDetail(stat.path)
		return scan(entryId, files, season)
	}

	private fun scan(entryId: String, files: List<FileStat>, season: String?): List<EntryScanResult> {
		val registeredVideos = season?.let { videoRepository.findByEntryIdAndSeason(entryId, season) }
			?: videoRepository.findNoSeasonVideos(entryId)
		log.debug { "[Entry Scan] registeredVideos=$registeredVideos" }

		val registeredVideoPaths = registeredVideos.map { it.path }

		val videoFiles = files.filter { isVideo(it.path) }

		val result1 = videoFiles
			.filter { it.path !in registeredVideoPaths }
			.map { registerVideo(it, entryId, files, season) }

		val result2 = videoFiles
			.filter { it.path in registeredVideoPaths }
			.mapNotNull { scanSubtitles(it, entryId, files, season) }

		return result1 + result2
	}

	fun registerVideo(videoFile: FileStat, entryId: String, files: List<FileStat>, season: String?): EntryScanResult {
		val videoName = Path(videoFile.path).nameWithoutExtension

		val thumbnailPath = videoRegisterService.generateThumbnail(videoFile.path)

		val video = Video(
			id = videoRepository.generateId(),
			path = videoFile.path,
			thumbnail = thumbnailPath,
			regDt = videoFile.mDate,
			entryId = entryId,
			season = season,
		)
		log.debug { "[Video Scan] video=$video" }

		val subtitles = files.filter { isSubtitle(it.path) }
			.filter { Path(it.path).name.startsWith(videoName) }
			.map {
				VideoSubtitle(
					id = videoSubtitleRepository.generateId(),
					path = it.path,
					videoId = video.id
				)
			}
		log.debug { "[Video Scan] subtitles=$subtitles" }

		videoRepository.save(video)
		videoSubtitleRepository.saveAll(subtitles)

		val metadata = videoMetadataService.scanAndSave(video.id)

		return EntryScanResult(video, metadata, subtitles)
	}

	fun scanSubtitles(videoFile: FileStat, entryId: String, files: List<FileStat>, season: String?): EntryScanResult? {
		val videoName = Path(videoFile.path).nameWithoutExtension
		val video = videoRepository.findByPath(videoFile.path) ?: return null
		val metadata = videoMetadataRepository.findByIdOrNull(video.path) ?: return null
		val subtitles = videoSubtitleRepository.findByVideoId(video.id)

		val oldSubtitlePaths = subtitles.map { it.path }
		val newSubtitlePaths = files.filter { isSubtitle(it.path) }
			.filter { Path(it.path).name.startsWith(videoName) }
			.map { it.path }

		val deleteSubtitlePaths = oldSubtitlePaths - newSubtitlePaths.toSet()
		val insertSubtitlePaths = newSubtitlePaths - oldSubtitlePaths.toSet()

		if (deleteSubtitlePaths.isEmpty() || insertSubtitlePaths.isEmpty()) {
			return null
		}

		log.debug { "[Scan Subtitle] deletes: $deleteSubtitlePaths" }
		log.debug { "[Scan Subtitle] inserts: $insertSubtitlePaths" }

		val deleteSubtitles = subtitles.filter { it.path in deleteSubtitlePaths }
		val insertSubtitles = insertSubtitlePaths.map {
			VideoSubtitle(
				id = videoSubtitleRepository.generateId(),
				path = it,
				videoId = video.id
			)
		}

		videoSubtitleRepository.saveAll(insertSubtitles)
		videoSubtitleRepository.deleteAll(deleteSubtitles)

		val newSubtitles = subtitles - deleteSubtitles.toSet() + insertSubtitles

		return EntryScanResult(video, metadata, newSubtitles)
	}
}