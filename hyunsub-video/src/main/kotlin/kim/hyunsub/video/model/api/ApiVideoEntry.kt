package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.VideoEntry

data class ApiVideoEntry(
	val id: String,
	val name: String,
	val thumbnail: String,
)

fun VideoEntry.toApi() = ApiVideoEntry(
	id = id,
	name = VideoEntry.parseTitle(name),
	thumbnail = FsPathConverter.thumbnailUrl(thumbnail)
)
