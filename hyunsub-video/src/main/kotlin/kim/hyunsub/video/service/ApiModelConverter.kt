package kim.hyunsub.video.service

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.common.util.getHumanReadableBitrate
import kim.hyunsub.common.util.getHumanReadableSize
import kim.hyunsub.video.model.api.RestApiVideo
import kim.hyunsub.video.model.api.RestApiVideoGroup
import kim.hyunsub.video.model.api.RestApiVideoMetadata
import kim.hyunsub.video.model.api.RestApiVideoSearchResult
import kim.hyunsub.video.model.api.RestApiVideoSubtitle
import kim.hyunsub.video.model.api.toApi
import kim.hyunsub.video.model.dto.VideoSearchResult
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoGroup
import kim.hyunsub.video.repository.entity.VideoHistory
import kim.hyunsub.video.repository.entity.VideoMetadata
import kim.hyunsub.video.repository.entity.VideoSubtitle
import org.springframework.stereotype.Service
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

@Service
class ApiModelConverter {
	fun convertVideo(
		video: Video,
		subtitles: List<VideoSubtitle>,
		metadata: VideoMetadata?,
		history: VideoHistory?,
	): RestApiVideo {
		return RestApiVideo(
			videoId = video.id,
			videoUrl = FsPathConverter.convertToUrl(video.path),
			thumbnailUrl = FsPathConverter.thumbnailUrl(video.thumbnail),
			title = Path(video.path).nameWithoutExtension,
			subtitles = subtitles.map { convertVideoSubtitle(video, it) },
			metadata = metadata?.let { convertVideoMetadata(it) },
			time = history?.time ?: 0,
		)
	}

	fun convertVideoSubtitle(video: Video, subtitle: VideoSubtitle): RestApiVideoSubtitle {
		val subtitleName = Path(subtitle.path).nameWithoutExtension
		val videoName = Path(video.path).nameWithoutExtension

		val code = subtitleName.replace(videoName, "")
			.replace(".", "")
			.ifEmpty { "ko" }

		val label = code
			.replace("en", "English ")
			.replace("ko", "Korean ")
			.replace("ja", "Japanese ")
			.trim()

		val url = Path(subtitle.path)
			.let { "${it.parent}/${it.nameWithoutExtension}.vtt" }
			.let { FsPathConverter.convertToUrl(it) }

		return RestApiVideoSubtitle(
			url = url,
			label = label,
			srclang = code,
		)
	}

	fun convertVideoMetadata(metadata: VideoMetadata): RestApiVideoMetadata {
		val duration = metadata.duration
		val sec = duration % 60
		val min = (duration / 60) % 60
		val hour = duration / 3600
		val durationStr = if (hour > 0) "${hour}시간 ${min}분" else "${min}분 ${sec}초"
		val sizeStr = getHumanReadableSize(metadata.size)
		val bitrateStr = getHumanReadableBitrate(metadata.bitrate)
		val resolution = "${metadata.width} x ${metadata.height}"

		return RestApiVideoMetadata(
			duration = durationStr,
			size = sizeStr,
			resolution = resolution,
			bitrate = bitrateStr,
		)
	}

	fun convertVideoSearchResult(result: VideoSearchResult): RestApiVideoSearchResult {
		val entries = result.entries.groupBy { it.category }
			.mapValues { entry -> entry.value.map { it.toApi() } }

		return RestApiVideoSearchResult(
			entries = entries
		)
	}

	fun convert(group: VideoGroup): RestApiVideoGroup {
		return RestApiVideoGroup(
			id = group.id,
			name = group.name,
		)
	}
}
