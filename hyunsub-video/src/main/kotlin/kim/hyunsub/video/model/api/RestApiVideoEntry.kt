package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.VideoEntry

data class RestApiVideoEntry(
	val id: String,
	val name: String,
	val thumbnail: String,
)

private val yearRegex = Regex(" \\(\\d{4}\\)")

fun VideoEntry.toApi() = RestApiVideoEntry(
	id = id,
	name = name.replace(yearRegex, ""),
	thumbnail = FsPathConverter.thumbnailUrl(thumbnail)
)
