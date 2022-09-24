package kim.hyunsub.video.service

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.log.Log
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.VideoRenameParams
import kim.hyunsub.video.model.VideoRenameResult
import kim.hyunsub.video.repository.*
import kim.hyunsub.video.repository.entity.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class VideoRenameService(
	private val apiCaller: ApiCaller,
	private val videoRepository: VideoRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoRenameHistoryRepository: VideoRenameHistoryRepository,
) {
	companion object : Log

	fun rename(params: VideoRenameParams): VideoRenameResult {
		log.debug("[Rename] params={}", params)

		val history = VideoRenameHistory(params)
		videoRenameHistoryRepository.save(history)

		val video = videoRepository.findByIdOrNull(params.videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("[Rename] video={}", video)

		val entry = videoEntryRepository.findByIdOrNull(video.videoEntryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		log.debug("[Rename] entry={}", entry)

		val subtitles = videoSubtitleRepository.findByVideoId(video.id)
		log.debug("[Rename] subtitles={}", subtitles)

		val metadata = videoMetadataRepository.findByIdOrNull(video.path)
		log.debug("[Rename] metadata={}", metadata)

		return VideoRenameResult(
			entry = renameEntry(entry, params),
			video = renameVideo(video, params),
			subtitles = subtitles.mapNotNull { renameSubtitle(it, params) },
			metadata = metadata?.let { renameMetadata(it, params) }
		)
	}

	fun renameVideo(video: Video, params: VideoRenameParams): Video? {
		var newVideo = video

		val oldVideoPath = video.path
		val newVideoPath = replace(video.path, params)
		log.debug("[Rename] video.path: {} -> {}", oldVideoPath, newVideoPath)
		if (oldVideoPath != newVideoPath) {
			apiCaller.rename(oldVideoPath, newVideoPath)
			newVideo = newVideo.copy(path = newVideoPath)
		}

		val oldThumbnailPath = video.thumbnail
		if (oldThumbnailPath != null) {
			val newThumbnailPath = replace(oldThumbnailPath, params)
			log.debug("[Rename] video.thumbnail: {} -> {}", oldThumbnailPath, newThumbnailPath)
			if (oldThumbnailPath != newThumbnailPath) {
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
		log.debug("[Rename] entry.name: {} -> {}", oldName, newName)
		if (oldName != newName) {
			newEntry = newEntry.copy(name = newName)
		}

		val oldThumbnailPath = entry.thumbnail
		if (oldThumbnailPath != null) {
			val newThumbnailPath = replace(oldThumbnailPath, params)
			log.debug("[Rename] entry.thumbnail: {} -> {}", oldThumbnailPath, newThumbnailPath)
			if (oldThumbnailPath != newThumbnailPath) {
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
		log.debug("[Rename] subtitle.path: {} -> {}", oldPath, newPath)
		if (oldPath != newPath) {
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
		log.debug("[Rename] metadata.path: {} -> {}", oldPath, newPath)
		if (oldPath != newPath) {
			val newMetadata = metadata.copy(path = newPath)
			videoMetadataRepository.save(newMetadata)
			videoMetadataRepository.delete(metadata)
			return newMetadata
		}
		return null
	}

	fun replace(str: String, params: VideoRenameParams) =
		if (params.isRegex)
			str.replace(Regex(params.from), params.to)
		else
			str.replace(params.from, params.to)
}
