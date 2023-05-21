package kim.hyunsub.common.fs.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class FileStat(
	val path: String,
	val mtime: Long,
	val size: Long,
	val isDir: Boolean?,
) {
	val mDate: LocalDateTime
		get() = mtime.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime() }
}
