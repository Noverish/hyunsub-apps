package kim.hyunsub.drive.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiMoveBulkParams
import kim.hyunsub.drive.model.DriveRemoveBulkParams
import kim.hyunsub.drive.model.FileInfo
import kim.hyunsub.drive.model.FileType
import kim.hyunsub.drive.model.PathParam
import kim.hyunsub.common.api.model.ApiRenameBulkParams
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.drive.service.DrivePathService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.io.path.Path

@RestController
@RequestMapping("/api/v1")
class DriveController(
	private val apiCaller: ApiCaller,
	private val drivePathService: DrivePathService,
) {
	@PostMapping("/list")
	fun list(@RequestBody params: PathParam): List<FileInfo> {
		return apiCaller.readdirDetail(params.path)
			.map { FileInfo(it) }
			.sortedBy { if (it.type == FileType.FOLDER) 0 else 1 }
	}

	@PostMapping("/download-text")
	fun downloadText(@RequestBody params: PathParam): ResponseEntity<String> {
		return ResponseEntity.ok()
			.contentType(MediaType.TEXT_PLAIN)
			.body(apiCaller.get(params.path))
	}

	@PostMapping("/rename-bulk")
	fun renameBulk(@RequestBody params: ApiRenameBulkParams): SimpleResponse {
		apiCaller.renameBulk(params)
		return SimpleResponse()
	}

	@PostMapping("/move-bulk")
	fun moveBulk(@RequestBody params: ApiMoveBulkParams): SimpleResponse {
		apiCaller.moveBulk(params)
		return SimpleResponse()
	}

	@PostMapping("/new-folder")
	fun newFolder(@RequestBody params: PathParam): SimpleResponse {
		apiCaller.mkdir(params.path)
		return SimpleResponse()
	}

	@PostMapping("/upload-session")
	fun uploadSession(
		userAuth: UserAuth,
		@RequestBody params: PathParam,
	): Map<String, String> {
		val basePath = drivePathService.getBasePath(userAuth)
		val path = Path(basePath, params.path).toString()
		val sessionKey = apiCaller.uploadSession(path)
		return mapOf("sessionKey" to sessionKey)
	}

	@PostMapping("/remove-bulk")
	fun remove(
		userAuth: UserAuth,
		@RequestBody params: DriveRemoveBulkParams,
	): SimpleResponse {
		val basePath = drivePathService.getBasePath(userAuth)
		val paths = params.paths.map { Path(basePath, it).toString() }
		apiCaller.removeBulk(paths)
		return SimpleResponse()
	}
}
