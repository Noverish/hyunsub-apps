package kim.hyunsub.video.service.scan

import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.random.RandomGenerator
import kim.hyunsub.video.model.ScanResult
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoSubtitle
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension

class VideoType2Scanner(
	private val randomGenerator: RandomGenerator,
	private val category: String,
	files: List<FileStat>,
) : VideoScanner {
	private val searcher = FileSearcher(files)
	private val videoEntries = mutableListOf<VideoEntry>()
	private val videos = mutableListOf<Video>()
	private val videoSubtitles = mutableListOf<VideoSubtitle>()

	override fun scan(path: String): ScanResult {
		videoEntries.clear()
		videos.clear()
		videoSubtitles.clear()

		searcher.readdir2(path)
			.filter { searcher.isDir(it) }
			.forEach { scanDepth1(it) }

		return ScanResult(emptyList(), videoEntries, videos, videoSubtitles)
	}

	private fun scanDepth1(path: String) {
		val filePaths = searcher.readdir2(path)

		val entryName = Path(path).name
		val thumbnailPath = filePaths.firstOrNull { Path(it).name == "thumbnail.jpg" }

		val hasVideo = filePaths.any { it.endsWith(".mp4") }
		val oldestVideoDate = if (hasVideo) {
			getOldestVideoDate(path)
		} else {
			filePaths
				.filter { searcher.isDir(it) }
				.map { getOldestVideoDate(it) }
				.min()
		}

		val videoEntry = VideoEntry(
			id = randomGenerator.generateRandomString(6),
			name = entryName,
			thumbnail = thumbnailPath,
			category = category,
			regDt = oldestVideoDate
		)
		videoEntries.add(videoEntry)

		if (hasVideo) {
			scanFolder(path, videoEntry)
		} else {
			filePaths
				.filter { searcher.isDir(it) }
				.forEach { scanFolder(it, videoEntry, Path(it).name) }
		}
	}

	private fun scanFolder(path: String, videoEntry: VideoEntry, season: String? = null) {
		val filePaths = searcher.readdir2(path)
		val videoPaths = filePaths.filter { it.endsWith(".mp4") }

		for(videoPath in videoPaths) {
			val videoDate = searcher.stat(videoPath)?.mDate ?: continue
			val videoName = Path(videoPath).nameWithoutExtension
			val thumbnailPath = filePaths.firstOrNull { Path(it).name == "$videoName.jpg" }

			val video = Video(
				id = randomGenerator.generateRandomString(6),
				path = videoPath,
				thumbnail = thumbnailPath,
				regDt = videoDate,
				videoEntryId = videoEntry.id,
				videoSeason = season,
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

	private fun getOldestVideoDate(path: String): LocalDateTime {
		return searcher.readdir2(path)
			.filter { it.endsWith(".mp4") }
			.mapNotNull { searcher.stat(it)?.mDate }
			.min()
	}
}
