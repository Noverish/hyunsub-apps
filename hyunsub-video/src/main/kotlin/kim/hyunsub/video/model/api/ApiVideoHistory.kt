package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.VideoMyHistory
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

data class ApiVideoHistory(
	val videoId: String,
	val time: Int,
	val date: LocalDateTime,
	val entryId: String,
	val duration: Int,
	val thumbnail: String?,
	val title: String,
)

fun VideoMyHistory.toApi() = ApiVideoHistory(
	videoId = videoId,
	time = time,
	date = date,
	entryId = entryId,
	duration = duration,
	thumbnail = FsPathConverter.thumbnailUrl(thumbnail),
	title = Path(path).nameWithoutExtension
)
