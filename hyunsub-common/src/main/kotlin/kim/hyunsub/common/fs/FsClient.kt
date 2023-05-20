package kim.hyunsub.common.fs

import kim.hyunsub.common.api.model.FileStat
import kim.hyunsub.common.fs.model.FsStatBulkParams
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "fsClient", url = "\${fs.host}")
interface FsClient {
	@GetMapping("/api/fs/stat")
	fun stat(@RequestParam path: String): FileStat

	@PostMapping("/api/fs/stat/bulk")
	fun statBulk(@RequestBody params: FsStatBulkParams): List<FileStat>

	@GetMapping("/api/fs/readdir")
	fun readdir(@RequestParam path: String): List<String>

	@GetMapping("/api/fs/readdir/detail")
	fun readdirDetail(@RequestParam path: String): List<FileStat>
}

fun FsClient.exist(path: String) =
	runCatching<FsClient, FileStat?> { stat(path) }
		.getOrElse { null }
		.let { it != null }

fun FsClient.statOrNull(path: String) =
	runCatching<FsClient, FileStat?> { stat(path) }
		.getOrElse { null }
