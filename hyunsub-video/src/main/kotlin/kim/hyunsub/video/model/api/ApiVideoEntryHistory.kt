package kim.hyunsub.video.model.api

import kim.hyunsub.common.fs.FsPathConverter
import kim.hyunsub.video.repository.entity.Video
import kim.hyunsub.video.repository.entity.VideoEntry
import kim.hyunsub.video.repository.entity.VideoEntryHistory
import java.time.LocalDateTime

data class ApiVideoEntryHistory(
	val entryId: String,
	val date: LocalDateTime,
	val videoId: String,
	val time: Int,
	val videoTitle: String,
	val entryTitle: String,
	val thumbnail: String,
	val category: String,
	val duration: Int,
)

fun VideoEntryHistory.toApi() = ApiVideoEntryHistory(
	entryId = entryId,
	date = date,
	videoId = videoId,
	time = time,
	videoTitle = Video.parseTitle(path),
	entryTitle = VideoEntry.parseTitle(name),
	thumbnail = FsPathConverter.thumbnailUrl(thumbnail),
	category = category,
	duration = duration,
)
