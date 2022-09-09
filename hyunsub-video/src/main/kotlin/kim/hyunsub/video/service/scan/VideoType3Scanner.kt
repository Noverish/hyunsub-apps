package kim.hyunsub.video.service.scan

import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.video.model.ScanResult
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoGroup
import kim.hyunsub.video.repository.entity.VideoSubtitle
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

class VideoType3Scanner(
	private val randomGenerator: RandomGenerator,
	private val category: String,
	files: List<FileStat>,
) : VideoScanner {
	private val searcher = FileSearcher(files)
	private val videoGroups = mutableListOf<VideoGroup>()
	private val videoEntries = mutableListOf<VideoEntry>()
	private val videos = mutableListOf<Video>()
	private val videoSubtitles = mutableListOf<VideoSubtitle>()

	override fun scan(path: String): ScanResult {
		videoGroups.clear()
		videoEntries.clear()
		videos.clear()
		videoSubtitles.clear()

		searcher.readdir2(path)
			.filter { searcher.isDir(it) }
			.forEach { scanDepth1(it) }

		return ScanResult(videoGroups, videoEntries, videos, videoSubtitles)
	}

	private fun scanDepth1(path: String) {
		val filePaths = searcher.readdir2(path)

		val videoGroup = VideoGroup(
			id = randomGenerator.generateRandomString(6),
			name = Path(path).name,
		)
		videoGroups.add(videoGroup)

		val videoPaths = filePaths.filter { it.endsWith(".mp4") }
		for (videoPath in videoPaths) {
			val videoDate = searcher.stat(videoPath)?.mDate ?: continue
			val videoName = Path(videoPath).nameWithoutExtension
			val thumbnailPath = filePaths.firstOrNull { it.endsWith("$videoName.jpg") }

			val videoEntry = VideoEntry(
				id = randomGenerator.generateRandomString(6),
				name = videoName,
				thumbnail = thumbnailPath,
				category = category,
				videoGroupId = videoGroup.id,
				regDt = videoDate,
			)
			videoEntries.add(videoEntry)

			val video = Video(
				id = randomGenerator.generateRandomString(6),
				path = videoPath,
				thumbnail = thumbnailPath,
				regDt = videoDate,
				videoEntryId = videoEntry.id,
			)
			videos.add(video)

			val subtitles = filePaths
				.filter { Path(it).name.startsWith(videoName) }
				.filter { it.endsWith(".srt") || it.endsWith(".smi") }
				.map {
					VideoSubtitle(
						id = randomGenerator.generateRandomString(6),
						path = it,
						videoId = video.id,
					)
				}
			videoSubtitles.addAll(subtitles)
		}
	}
}
