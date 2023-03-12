package kim.hyunsub.photo.util

import kotlin.io.path.Path
import kotlin.io.path.nameWithoutExtension

object PhotoPathUtils {
	fun tmp(nonce: String) = Path("/hyunsub/file/upload", nonce).toString()
	fun original(file: String, year: Int) = Path("/hyunsub/photo/original", year.toString(), file).toString()
	fun thumbnail(file: String, year: Int) = Path("/hyunsub/photo/thumbnail", year.toString(), Path(file).nameWithoutExtension + ".jpg").toString()
}
