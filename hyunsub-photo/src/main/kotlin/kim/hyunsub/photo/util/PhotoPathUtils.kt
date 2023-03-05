package kim.hyunsub.photo.util

import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

object PhotoPathUtils {
	fun tmp(file: String) = Path("/hyunsub/photo/tmp", file).toString()
	fun original(file: String, year: Int) = Path("/hyunsub/photo/original", year.toString(), file).toString()
	fun thumbnail(file: String, year: Int) = Path("/hyunsub/photo/thumbnail", year.toString(), Path(file).nameWithoutExtension + ".jpg").toString()
}
