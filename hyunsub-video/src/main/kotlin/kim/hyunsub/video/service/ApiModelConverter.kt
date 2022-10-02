package kim.hyunsub.video.service

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.common.util.getHumanReadableBitrate
import kim.hyunsub.common.util.getHumanReadableSize
import kim.hyunsub.video.model.*
import kim.hyunsub.video.repository.entity.*
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

@Service
class ApiModelConverter(
	private val fileUrlConverter: FileUrlConverter,
) {
	fun convert(entry: VideoEntry): RestVideoEntry {
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
		val sizeStr = getHumanReadableSize(metadata.size)
		val bitrateStr = getHumanReadableBitrate(metadata.bitrate)
		val resolution = "${metadata.width} x ${metadata.height}"

		return RestVideoMetadata(
			duration = durationStr,
			size = sizeStr,
			resolution = resolution,
			bitrate = bitrateStr,
		)
	}

	fun convertVideoSearchResult(result: VideoSearchResult): RestVideoSearchResult {
		val entries = result.entries.groupBy { it.category }
			.mapValues { entry -> entry.value.map { convert(it) } }

		return RestVideoSearchResult(
			entries = entries
		)
	}

	fun convert(group: VideoGroup): RestApiVideoGroupPreview {
		return RestApiVideoGroupPreview(
			id = group.id,
			name = group.name,
		)
	}
}
