package kim.hyunsub.video.bo

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.model.FsRenameBulkData
import kim.hyunsub.common.fs.model.FsRenameBulkParams
import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.video.model.dto.VideoRenameParams
import kim.hyunsub.video.repository.VideoMetadataRepository
import kim.hyunsub.video.repository.VideoRepository
import kim.hyunsub.video.repository.VideoSubtitleRepository
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

@Service
class VideoRenameBo(
	private val fsClient: FsClient,
	private val videoRepository: VideoRepository,
	private val videoMetadataRepository: VideoMetadataRepository,
	private val videoSubtitleRepository: VideoSubtitleRepository,
) {
	private val log = KotlinLogging.logger { }

	@Transactional
	fun rename(videoId: String, params: VideoRenameParams): List<FsRenameBulkData> {
		val renames = mutableListOf<FsRenameBulkData>()

		val video = videoRepository.findByIdOrNull(videoId)
			?: throw ErrorCodeException(ErrorCode.NOT_FOUND, "No such video")

		val newVideo = video.copy(
			path = replaceFileName(video.path, params, renames),
			thumbnail = video.thumbnail?.let { replaceFileName(it, params, renames) },
		)

		val subtitles = videoSubtitleRepository.findByVideoId(videoId)
		val newSubtitles = subtitles.map {
			it.copy(path = replaceFileName(it.path, params, renames))
		}

		val parent = Path(video.path).parent.toString()
		fsClient.renameBulk(FsRenameBulkParams(parent, renames))

		videoMetadataRepository.updatePath(video.path, newVideo.path)
		videoRepository.save(newVideo)
		videoSubtitleRepository.saveAll(newSubtitles)

		return renames
	}

	private fun replaceFileName(path: String, params: VideoRenameParams, renames: MutableList<FsRenameBulkData>): String {
		val pathObj = Path(path)
		val parent = pathObj.parent.toString()
		val name = pathObj.nameWithoutExtension
		val ext = pathObj.extension
		val fileName = "$name.$ext"

		val newName = name.replace(params.from, params.to)
		val newFileName = "$newName.$ext"

		val newPath = Path(parent, "$newName.$ext").toString()
		log.debug { "[Video Rename] $path -> $newPath" }
		renames.add(FsRenameBulkData(fileName, newFileName))
		return newPath
	}
}
