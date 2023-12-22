package kim.hyunsub.video.service

import jakarta.transaction.Transactional
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.model.FsRenameBulkData
import kim.hyunsub.common.fs.model.FsRenameBulkParams
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.dto.EntryRenameParams
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRenameHistoryRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoRenameHistory
import kim.hyunsub.video.repository.entity.VideoSubtitle
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.relativeTo

@Service
class EntryRenameService(
	private val fsClient: FsClient,
	private val videoRepository: VideoRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
	private val videoRenameHistoryRepository: VideoRenameHistoryRepository,
) {
	private val log = KotlinLogging.logger { }

	@Transactional
	fun rename(entryId: String, params: EntryRenameParams): List<FsRenameBulkData> {
		val entryPath = videoEntryRepository.findByIdOrNull(entryId)?.thumbnail
			?.let { Path(it).parent }
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)

		val videos = videoRepository.findByEntryId(entryId)
		val videoIds = videos.map { it.id }

		val videoResults = iterateVideos(videos, params)

		val subtitleResults = iterateSubtitles(videoIds, params)

		val renames = buildList {
			videoResults.forEach {
				add(FsRenameBulkData(it.first.path, it.second.path))

				val oldThumbnail = it.first.thumbnail
				val newThumbnail = it.second.thumbnail
				if (oldThumbnail != null && newThumbnail != null) {
					add(FsRenameBulkData(oldThumbnail, newThumbnail))
				}
			}

			subtitleResults.forEach {
				add(FsRenameBulkData(it.first.path, it.second.path))
			}
		}.map {
			val from = Path(it.from).relativeTo(entryPath).toString()
			val to = Path(it.to).relativeTo(entryPath).toString()
			FsRenameBulkData(from, to)
		}

		log.debug { "[Entry Rename] renames=$renames" }

		fsClient.renameBulk(FsRenameBulkParams(entryPath.toString(), renames))

		// Video update 전에 해야 함 (it.first.path 가 바뀌기 때문)
		iterateMetadatas(videoResults.map { it.first.path }, params)
		videoRepository.saveAll(videoResults.map { it.second })
		videoSubtitleRepository.saveAll(subtitleResults.map { it.second })

		val histories = videoResults.map {
			VideoRenameHistory(
				videoId = it.second.id,
				from = params.from,
				to = params.to,
				isRegex = true,
			)
		}
		videoRenameHistoryRepository.saveAll(histories)

		return renames
	}

	private fun iterateVideos(videos: List<Video>, params: EntryRenameParams): List<Pair<Video, Video>> {
		val result = mutableListOf<Pair<Video, Video>>()

		for (video in videos) {
			val path = video.path
			val thumbnail = video.thumbnail

			val newPath = replace(path, params)
			val newThumbnail = thumbnail?.let { replace(it, params) }

			if (path != newPath || thumbnail != newThumbnail) {
				val newVideo = video.copy(path = newPath, thumbnail = newThumbnail)
				result.add(video to newVideo)
			}
		}

		return result
	}

	private fun iterateSubtitles(
		videoIds: List<String>,
		params: EntryRenameParams,
	): List<Pair<VideoSubtitle, VideoSubtitle>> {
		val subtitles = videoSubtitleRepository.findByVideoIdIn(videoIds)
		val result = mutableListOf<Pair<VideoSubtitle, VideoSubtitle>>()

		for (subtitle in subtitles) {
			val path = subtitle.path
			val newPath = replace(path, params)
			if (path != newPath) {
				val newSubtitle = subtitle.copy(path = newPath)
				result.add(subtitle to newSubtitle)
			}
		}

		return result
	}

	private fun iterateMetadatas(paths: List<String>, params: EntryRenameParams) {
		val metadatas = videoMetadataRepository.findAllById(paths)
		val deletes = mutableListOf<VideoMetadata>()
		val inserts = mutableListOf<VideoMetadata>()

		for (metadata in metadatas) {
			val path = metadata.path
			val newPath = replace(path, params)

			deletes.add(metadata)
			inserts.add(metadata.copy(path = newPath))
		}

		log.debug { "[Entry Rename] metadata to insert: $inserts" }
		log.debug { "[Entry Rename] metadata to delete: $deletes" }

		videoMetadataRepository.saveAll(inserts)
		videoMetadataRepository.deleteAll(deletes)
	}

	private fun replace(path: String, params: EntryRenameParams) =
		path.replace(Regex(params.from), params.to)
}
