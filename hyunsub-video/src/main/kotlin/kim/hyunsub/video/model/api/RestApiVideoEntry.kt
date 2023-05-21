package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.VideoEntry

data class RestApiVideoEntry(
	val id: String,
	val name: String,
	val thumbnail: String,
) {
	companion object {
		private val yearRegex = Regex(" \\(\\d{4}\\)")
	}

	constructor(entity: VideoEntry) : this(
		id = entity.id,
		name = entity.name.replace(yearRegex, ""),
		thumbnail = FsPathConverter.thumbnailUrl(entity.thumbnail)
	)
}
