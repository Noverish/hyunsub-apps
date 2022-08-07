package kim.hyunsub.video.service

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.video.model.*
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

@Service
class RestModelConverter(
	private val fileUrlConverter: FileUrlConverter,
) {
	fun convertVideoEntry(entry: VideoEntry): RestVideoEntry {
		val yearRegex = Regex(" \\(\\d{4}\\)")
		val name = entry.name.replace(yearRegex, "")

		val thumbnail = entry.thumbnail?.let { fileUrlConverter.pathToUrl(it) }
			?: "/img/placeholder.jpg"

		return RestVideoEntry(
			id = entry.id,
			name = name,
			thumbnail = thumbnail
		)
	}

	fun convertVideo(video: Video, subtitles: List<VideoSubtitle>, metadata: VideoMetadata?): RestVideo {
		val thumbnailUrl = video.thumbnail?.let { fileUrlConverter.pathToUrl(it) }
			?: "/img/placeholder.jpg"

		return RestVideo(
			videoId = video.id,
			videoUrl = fileUrlConverter.pathToUrl(video.path),
			thumbnailUrl = thumbnailUrl,
			title = Path(video.path).nameWithoutExtension,
			subtitles = subtitles.map { convertVideoSubtitle(video, it) },
			metadata = metadata?.let { convertVideoMetadata(it) },
		)
	}

	fun convertToEpisode(video: Video): RestVideoEpisode {
		val thumbnailUrl = video.thumbnail?.let { fileUrlConverter.pathToUrl(it) }
			?: "/img/placeholder.jpg"

		return RestVideoEpisode(
			videoId = video.id,
			thumbnailUrl = thumbnailUrl,
			title = Path(video.path).nameWithoutExtension,
		)
	}

	fun convertVideoSubtitle(video: Video, subtitle: VideoSubtitle): RestVideoSubtitle {
		val subtitleName = Path(subtitle.path).nameWithoutExtension
		val videoName = Path(video.path).nameWithoutExtension

		val code = subtitleName.replace(videoName, "")
			.replace(".", "")
			.ifEmpty { "ko" }

		val srclang = code.substring(0, 2)
		val label = code
			.replace("en", "English ")
			.replace("ko", "Korean ")
			.trim()

		val url = Path(subtitle.path)
			.let { "${it.parent}/${it.nameWithoutExtension}.vtt" }
			.let { fileUrlConverter.pathToUrl(it) }

		return RestVideoSubtitle(
			url = url,
			label = label,
			srclang = srclang,
		)
	}

	fun convertVideoMetadata(metadata: VideoMetadata): RestVideoMetadata {
		val duration = metadata.duration
		val sec = duration % 60
		val min = (duration / 60) % 60
		val hour = duration / 3600
		val durationStr = if (hour > 0) "${hour}시간 ${min}분" else "${min}분 ${sec}초"

		val size = metadata.size
		val mb = 1000 * 1000
		val gb = 1000 * 1000 * 1000
		val sizeStr = if (size > gb) {
			val tmp = String.format("%.2f", size.toDouble() / gb.toDouble())
			"$tmp GB"
		} else {
			val tmp = String.format("%.2f", size.toDouble() / mb.toDouble())
			"$tmp MB"
		}

		val resolution = "${metadata.width} x ${metadata.height}"

		val bitrate = metadata.bitrate
		val kb = 1000
		val bitrateStr = if (bitrate > mb) {
			val tmp = String.format("%.2f", bitrate.toDouble() / mb.toDouble())
			"$tmp mbp/s"
		} else {
			val tmp = String.format("%.2f", bitrate.toDouble() / kb.toDouble())
			"$tmp kbp/s"
		}

		return RestVideoMetadata(
			duration = durationStr,
			size = sizeStr,
			resolution = resolution,
			bitrate = bitrateStr,
		)
	}
}
