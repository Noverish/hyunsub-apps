package kim.hyunsub.video.service.scan

import kim.hyunsub.common.api.model.FileStat
import kotlin.io.path.Path

class FileSearcher(private val files: List<FileStat>) {
	fun readdir(path: String): List<String> {
		val pathWithSlash = if (path.endsWith("/")) path else "$path/"

		return files
			.map { it.path }
			.filter { it.startsWith(pathWithSlash) }
			.map { it.replaceFirst(pathWithSlash, "") }
			.map { it.split("/")[0] }
			.toSet() // For remove duplicates, make set and then make list
			.toList()
	}

	fun readdir2(path: String): List<String>
			= readdir(path).map { Path(path, it).toString() }

	fun isDir(path: String): Boolean = files.map { it.path }.any { it.startsWith(path) && it != path }

	fun stat(path: String): FileStat? = files.firstOrNull { it.path == path }
}
