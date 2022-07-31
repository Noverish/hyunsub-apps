package kim.hyunsub.video.service

import kim.hyunsub.common.api.FileUrlConverter
import kim.hyunsub.video.model.RestVideoEntry
import kim.hyunsub.video.repository.entity.VideoEntry
import org.springframework.stereotype.Service

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
}
