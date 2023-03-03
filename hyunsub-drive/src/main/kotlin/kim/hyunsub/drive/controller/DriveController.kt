package kim.hyunsub.drive.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.ApiMoveBulkParams
import kim.hyunsub.common.api.model.ApiRenameBulkParams
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.drive.model.DriveRemoveBulkParams
import kim.hyunsub.drive.model.FileInfo
import kim.hyunsub.drive.model.FileType
import kim.hyunsub.drive.model.PathParam
import kim.hyunsub.drive.service.DrivePathService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DriveController(
	private val apiCaller: ApiCaller,
	private val drivePathService: DrivePathService,
) {
	@PostMapping("/list")
	fun list(
		userAuth: UserAuth,
		@RequestBody params: PathParam,
	): List<FileInfo> {
		val path = drivePathService.getPath(userAuth, params.path)
		return apiCaller.readdirDetail(path)
			.map { FileInfo(it) }
			.sortedBy { if (it.type == FileType.FOLDER) 0 else 1 }
	}

	@PostMapping("/rename-bulk")
	fun renameBulk(
		userAuth: UserAuth,
		@RequestBody params: ApiRenameBulkParams,
	): SimpleResponse {
		val newParams = params.copy(path = drivePathService.getPath(userAuth, params.path))
		apiCaller.renameBulk(newParams)
		return SimpleResponse()
	}

	@PostMapping("/move-bulk")
	fun moveBulk(
		userAuth: UserAuth,
		@RequestBody params: ApiMoveBulkParams
	): SimpleResponse {
		val newParams = params.copy(
			from = drivePathService.getPath(userAuth, params.from),
			to = drivePathService.getPath(userAuth, params.to),
		)
		apiCaller.moveBulk(newParams)
		return SimpleResponse()
	}

	@PostMapping("/new-folder")
	fun newFolder(
		userAuth: UserAuth,
		@RequestBody params: PathParam,
	): SimpleResponse {
		apiCaller.mkdir(drivePathService.getPath(userAuth, params.path))
		return SimpleResponse()
	}

	@PostMapping("/upload-session")
	fun uploadSession(
		userAuth: UserAuth,
		@RequestBody params: PathParam,
	): Map<String, String> {
		val path = drivePathService.getPath(userAuth, params.path)
		val sessionKey = apiCaller.uploadSession(path)
		return mapOf("sessionKey" to sessionKey)
	}

	@PostMapping("/remove-bulk")
	fun remove(
		userAuth: UserAuth,
		@RequestBody params: DriveRemoveBulkParams,
	): SimpleResponse {
		val paths = params.paths.map { drivePathService.getPath(userAuth, it) }
		apiCaller.removeBulk(paths)
		return SimpleResponse()
	}
}
