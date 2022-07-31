package kim.hyunsub.video.service

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.video.model.RestVideoDetail
import kim.hyunsub.video.model.RestVideoEntry
import kim.hyunsub.video.model.RestVideoSubtitle
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.extension
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

	fun convertVideoDetail(entry: VideoEntry, video: Video, subtitles: List<VideoSubtitle>): RestVideoDetail {
		val thumbnail = video.thumbnail?.let { fileUrlConverter.pathToUrl(it) }
			?: "/img/placeholder.jpg"

		return RestVideoDetail(
			videoUrl = fileUrlConverter.pathToUrl(video.path),
			thumbnailUrl = thumbnail,
			title = Path(video.path).nameWithoutExtension,
			subtitles = subtitles.map { convertVideoSubtitle(video, it) }
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
}
