package kim.hyunsub.drive.model

import kim.hyunsub.common.api.model.FileStat
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.name

data class FileInfo(
	val name: String,
	val size: Long,
	val date: LocalDateTime,
	val isDir: Boolean,
) {
	constructor(stat: FileStat) : this(
		name = Path(stat.path).name,
		size = if (stat.isDir == true) 0 else stat.size,
		date = stat.mDate,
		isDir = stat.isDir == true,
	)
}
