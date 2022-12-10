package kim.hyunsub.drive.model

import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.util.getHumanReadableSize
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.name

data class FileInfo(
	val name: String,
	val size: String,
	val date: LocalDateTime,
	val type: FileType,
) {
	constructor(stat: FileStat): this(
		name = Path(stat.path).name,
		size = if (stat.isDir == true) "" else getHumanReadableSize(stat.size),
		date = stat.mDate,
		type = if (stat.isDir == true) FileType.FOLDER else FileType.fromPath(stat.path)
	)
}
