package kim.hyunsub.common.fs.model

import kim.hyunsub.common.util.toLdt
import java.time.LocalDateTime

data class FileStat(
	val path: String,
	val mtime: Long,
	val size: Long,
	val isDir: Boolean?,
) {
	val mDate: LocalDateTime
		get() = mtime.toLdt()
}
