package kim.hyunsub.drive.controller

import kim.hyunsub.common.fs.client.FsClient
import kim.hyunsub.common.fs.client.mkdir
import kim.hyunsub.common.fs.client.removeBulk
import kim.hyunsub.common.fs.model.FsMoveBulkParams
import kim.hyunsub.common.fs.model.FsRenameBulkParams
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.drive.model.DriveRemoveBulkParams
import kim.hyunsub.drive.model.FileInfo
import kim.hyunsub.drive.model.PathParam
import kim.hyunsub.drive.service.DrivePathService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DriveController(
	private val fsClient: FsClient,
	private val drivePathService: DrivePathService,
) {
	@PostMapping("/list")
	fun list(
		userAuth: UserAuth,
		@RequestBody params: PathParam,
	): List<FileInfo> {
		val path = drivePathService.getPath(userAuth, params.path)
		return fsClient.readdirDetail(path)
			.map { FileInfo(it) }
	}

	@PostMapping("/rename-bulk")
	fun renameBulk(
		userAuth: UserAuth,
		@RequestBody params: FsRenameBulkParams,
	): SimpleResponse {
		val newParams = params.copy(path = drivePathService.getPath(userAuth, params.path))
		fsClient.renameBulk(newParams)
		return SimpleResponse()
	}

	@PostMapping("/move-bulk")
	fun moveBulk(
		userAuth: UserAuth,
		@RequestBody params: FsMoveBulkParams,
	): SimpleResponse {
		val newParams = params.copy(
			from = drivePathService.getPath(userAuth, params.from),
			to = drivePathService.getPath(userAuth, params.to),
		)
		fsClient.moveBulk(newParams)
		return SimpleResponse()
	}

	@PostMapping("/new-folder")
	fun newFolder(
		userAuth: UserAuth,
		@RequestBody params: PathParam,
	): SimpleResponse {
		fsClient.mkdir(drivePathService.getPath(userAuth, params.path))
		return SimpleResponse()
	}

	@PostMapping("/remove-bulk")
	fun remove(
		userAuth: UserAuth,
		@RequestBody params: DriveRemoveBulkParams,
	): SimpleResponse {
		val paths = params.paths.map { drivePathService.getPath(userAuth, it) }
		fsClient.removeBulk(paths)
		return SimpleResponse()
	}
}
