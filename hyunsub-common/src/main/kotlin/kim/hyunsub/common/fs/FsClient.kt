package kim.hyunsub.common.fs

import kim.hyunsub.common.fs.model.FileStat
import kim.hyunsub.common.fs.model.FsCopyMDateParams
import kim.hyunsub.common.fs.model.FsHashResult
import kim.hyunsub.common.fs.model.FsMoveBulkParams
import kim.hyunsub.common.fs.model.FsPathParams
import kim.hyunsub.common.fs.model.FsPathsParams
import kim.hyunsub.common.fs.model.FsRenameBulkParams
import kim.hyunsub.common.fs.model.FsRenameParams
import kim.hyunsub.common.fs.model.FsRsyncParams
import kim.hyunsub.common.fs.model.FsRsyncResult
import kim.hyunsub.common.fs.model.FsSimpleResult
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

	@PostMapping("/api/fs/mkdir")
	fun mkdir(@RequestBody params: FsPathParams): FsSimpleResult

	@PostMapping("/api/fs/rename")
	fun rename(@RequestBody params: FsRenameParams): FsSimpleResult

	@PostMapping("/api/fs/rename/bulk")
	fun renameBulk(@RequestBody params: FsRenameBulkParams): FsSimpleResult

	@PostMapping("/api/fs/move/bulk")
	fun moveBulk(@RequestBody params: FsMoveBulkParams): FsSimpleResult

	@PostMapping("/api/fs/remove")
	fun remove(@RequestBody params: FsPathParams): FsSimpleResult

	@PostMapping("/api/fs/remove/bulk")
	fun removeBulk(@RequestBody params: FsPathsParams): FsSimpleResult

	@PostMapping("/api/fs/copy-mdate")
	fun copyMDate(@RequestBody params: FsCopyMDateParams): FsSimpleResult

	@GetMapping("/api/fs/hash")
	fun hash(@RequestParam path: String): FsHashResult

	@PostMapping("/api/fs/rsync")
	fun rsync(@RequestBody params: FsRsyncParams): FsRsyncResult
}

fun FsClient.exist(path: String) =
	runCatching<FsClient, FileStat?> { stat(path) }
		.getOrElse { null }
		.let { it != null }

fun FsClient.statOrNull(path: String) =
	runCatching<FsClient, FileStat?> { stat(path) }
		.getOrElse { null }

fun FsClient.mkdir(path: String) =
	mkdir(FsPathParams(path))

fun FsClient.rename(from: String, to: String, override: Boolean = false) =
	rename(FsRenameParams(from, to, override))

fun FsClient.remove(path: String) =
	remove(FsPathParams(path))

fun FsClient.removeBulk(paths: List<String>) =
	removeBulk(FsPathsParams(paths))

fun FsClient.copyMDate(from: String, to: String) =
	copyMDate(FsCopyMDateParams(from, to))
