package kim.hyunsub.video.repository.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import kim.hyunsub.common.fs.FsPathConverter
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

data class VideoMyHistory(
	val videoId: String,
	val time: Int,
	val date: LocalDateTime,
	val entryId: String,
	val duration: Int,
	@field:JsonIgnore val thumbnail: String?,
	@field:JsonIgnore val path: String,
) {
	val title: String
		get() = Path(path).nameWithoutExtension

	val thumbnailUrl: String
		get() = FsPathConverter.thumbnailUrl(thumbnail)
}
