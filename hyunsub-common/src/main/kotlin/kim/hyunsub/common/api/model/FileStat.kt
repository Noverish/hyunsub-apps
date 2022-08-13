package kim.hyunsub.common.api.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class FileStat(
	val path: String,
	val mtime: Long,
	val size: Long?,
) {
	val mDate: LocalDateTime
		get() = mtime.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
}
