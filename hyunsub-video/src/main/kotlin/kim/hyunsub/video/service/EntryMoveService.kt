package kim.hyunsub.video.service

import jakarta.transaction.Transactional
import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.rename
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.dto.EntryMoveParams
import kim.hyunsub.video.repository.VideoEntryRepository
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EntryMoveService(
	private val fsClient: FsClient,
	private val videoRepository: VideoRepository,
	private val videoEntryRepository: VideoEntryRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
) {
	private val log = KotlinLogging.logger { }

	@Transactional
	fun move(entryId: String, params: EntryMoveParams) {
		// update entry
		val entry = videoEntryRepository.findByIdOrNull(entryId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND)
		entry.thumbnail = entry.thumbnail?.let { processPath(it, params) }
		log.debug { "[Entry Move] entry.thumbnail=${entry.thumbnail}" }
		videoEntryRepository.saveAndFlush(entry)

		// update videos and its metadata and record history
		val videos = videoRepository.findByEntryId(entryId)
		videos.forEach { video ->
			val oldPath = video.path
			val newPath = processPath(video.path, params)

			videoMetadataRepository.updatePath(oldPath, newPath)

			video.path = newPath
			video.thumbnail = video.thumbnail?.let { processPath(it, params) }
			log.debug { "[Entry Move] video.path=${video.path}" }
			log.debug { "[Entry Move] video.thumbnail=${video.thumbnail}" }
		}
		videoRepository.saveAll(videos)

		// update subtitles
		val videoIds = videos.map { it.id }
		val subtitles = videoSubtitleRepository.findByVideoIdIn(videoIds)
		subtitles.forEach { subtitle ->
			subtitle.path = processPath(subtitle.path, params)
			log.debug { "[Entry Move] subtitle.path=${subtitle.path}" }
		}
		videoSubtitleRepository.saveAll(subtitles)

		// move folder
		fsClient.rename(params.oldPath, params.newPath)
	}

	private fun processPath(path: String, params: EntryMoveParams): String {
		return path.replace(params.oldPath, params.newPath)
	}
}
