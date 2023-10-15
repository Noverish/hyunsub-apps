package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.VideoEntry

data class RestApiVideoEntry(
	val id: String,
	val name: String,
	val thumbnail: String,
)

fun VideoEntry.toApi() = RestApiVideoEntry(
	id = id,
	name = VideoEntry.parseTitle(name),
	thumbnail = FsPathConverter.thumbnailUrl(thumbnail)
)
