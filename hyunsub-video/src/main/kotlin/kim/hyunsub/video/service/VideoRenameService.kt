package kim.hyunsub.video.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.VideoRenameParams
import kim.hyunsub.video.model.VideoRenameResult
import kim.hyunsub.video.model.dto.VideoRenameBulkParams
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRenameHistoryRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.name

@Service
class VideoRenameService(
	private val apiCaller: ApiCaller,
	private val videoRepository: VideoRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoRenameHistoryRepository: VideoRenameHistoryRepository,
) {
	private val log = KotlinLogging.logger { }

	fun renameBulk(params: VideoRenameBulkParams): List<VideoRenameResult> {
		log.debug("[renameBulk] params={}", params)

		return params.videoIds.map {
			rename(
				VideoRenameParams(
					videoId = it,
					from = params.from,
					to = params.to,
					isRegex = params.isRegex
				),
				renameEntry = false,
			)
		}
	}

	fun rename(params: VideoRenameParams, renameEntry: Boolean): VideoRenameResult {
		log.debug("[Rename] params={}", params)

		val video = videoRepository.findByIdOrNull(params.videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("[Rename] video={}", video)

		if (params.isRegex && !video.path.contains(Regex(params.from))) {
			return VideoRenameResult.empty()
		}
		if (!params.isRegex && !video.path.contains(params.from)) {
			return VideoRenameResult.empty()
		}

		val history = params.toEntity()
		log.debug("[Rename] history={}", history)
		videoRenameHistoryRepository.save(history)

		val entry = if (renameEntry) {
			val entry = videoEntryRepository.findByIdOrNull(video.videoEntryId)
				?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
			log.debug("[Rename] entry={}", entry)
			renameEntry(entry, params)
		} else {
			null
		}

		val subtitles = videoSubtitleRepository.findByVideoId(video.id)
		log.debug("[Rename] subtitles={}", subtitles)

		val metadata = videoMetadataRepository.findByIdOrNull(video.path)
		log.debug("[Rename] metadata={}", metadata)

		return VideoRenameResult(
			entry = entry,
			video = renameVideo(video, params),
			subtitles = subtitles.mapNotNull { renameSubtitle(it, params) },
			metadata = metadata?.let { renameMetadata(it, params) }
		)
	}

	fun renameVideo(video: Video, params: VideoRenameParams): Video? {
		var newVideo = video

		val oldVideoPath = video.path
		val newVideoPath = replace(video.path, params)
		if (oldVideoPath != newVideoPath) {
			log.debug("[Rename] video.path: {} -> {}", oldVideoPath, newVideoPath)
			apiCaller.rename(oldVideoPath, newVideoPath)
			newVideo = newVideo.copy(path = newVideoPath)
		}

		val oldThumbnailPath = video.thumbnail
		if (oldThumbnailPath != null) {
			val newThumbnailPath = replace(oldThumbnailPath, params)
			if (oldThumbnailPath != newThumbnailPath) {
				log.debug("[Rename] video.thumbnail: {} -> {}", oldThumbnailPath, newThumbnailPath)
				apiCaller.rename(oldThumbnailPath, newThumbnailPath)
				newVideo = newVideo.copy(thumbnail = newThumbnailPath)
			}
		}

		if (newVideo != video) {
			videoRepository.save(newVideo)
			return newVideo
		}

		return null
	}

	fun renameEntry(entry: VideoEntry, params: VideoRenameParams): VideoEntry? {
		var newEntry = entry

		val oldName = entry.name
		val newName = replace(entry.name, params)
		if (oldName != newName) {
			log.debug("[Rename] entry.name: {} -> {}", oldName, newName)
			newEntry = newEntry.copy(name = newName)
		}

		val oldThumbnailPath = entry.thumbnail
		if (oldThumbnailPath != null) {
			val newThumbnailPath = replace(oldThumbnailPath, params)
			if (oldThumbnailPath != newThumbnailPath) {
				log.debug("[Rename] entry.thumbnail: {} -> {}", oldThumbnailPath, newThumbnailPath)
				newEntry = newEntry.copy(thumbnail = newThumbnailPath)
			}
		}

		if (newEntry != entry) {
			videoEntryRepository.save(newEntry)
			return newEntry
		}

		return null
	}

	fun renameSubtitle(subtitle: VideoSubtitle, params: VideoRenameParams): VideoSubtitle? {
		val oldPath = subtitle.path
		val newPath = replace(subtitle.path, params)
		if (oldPath != newPath) {
			log.debug("[Rename] subtitle.path: {} -> {}", oldPath, newPath)
			val newSubtitle = subtitle.copy(path = newPath)
			videoSubtitleRepository.save(newSubtitle)
			apiCaller.rename(oldPath, newPath)
			return newSubtitle
		}
		return null
	}

	fun renameMetadata(metadata: VideoMetadata, params: VideoRenameParams): VideoMetadata? {
		val oldPath = metadata.path
		val newPath = replace(metadata.path, params)
		if (oldPath != newPath) {
			log.debug("[Rename] metadata.path: {} -> {}", oldPath, newPath)
			val newMetadata = metadata.copy(path = newPath)
			videoMetadataRepository.save(newMetadata)
			videoMetadataRepository.delete(metadata)
			return newMetadata
		}
		return null
	}

	fun replace(str: String, params: VideoRenameParams): String {
		if (str.startsWith("/")) {
			val parent = Path(str).parent.toString()
			val name = Path(str).name

			val newName = if (params.isRegex) {
				name.replace(Regex(params.from), params.to)
			} else {
				name.replace(params.from, params.to)
			}

			return Path(parent, newName).toString()
		} else {
			return if (params.isRegex) {
				str.replace(Regex(params.from), params.to)
			} else {
				str.replace(params.from, params.to)
			}
		}
	}
}
