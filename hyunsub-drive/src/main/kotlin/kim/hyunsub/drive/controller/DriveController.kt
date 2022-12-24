package kim.hyunsub.drive.controller

import kim.hyunsub.common.api.ApiCaller
import kim.hyunsub.common.api.model.MoveBulkParams
import kim.hyunsub.common.web.config.WebConstants
import kim.hyunsub.drive.model.FileInfo
import kim.hyunsub.drive.model.FileType
import kim.hyunsub.drive.model.PathParam
import kim.hyunsub.common.api.model.RenameBulkParams
import kim.hyunsub.common.web.model.SimpleResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DriveController(
	private val apiCaller: ApiCaller,
) {
	@PostMapping("/list")
	fun list(
		@RequestBody params: PathParam,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String?,
	): List<FileInfo> {
		return apiCaller.readdirDetail(params.path, token = token)
			.map { FileInfo(it) }
			.sortedBy { if (it.type == FileType.FOLDER) 0 else 1 }
	}

	@PostMapping("/download-text")
	fun downloadText(
		@RequestBody params: PathParam,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String?,
	): ResponseEntity<String> {
		return ResponseEntity.ok()
			.contentType(MediaType.TEXT_PLAIN)
			.body(apiCaller.get(params.path, token = token))
	}

	@PostMapping("/rename-bulk")
	fun renameBulk(
		@RequestBody params: RenameBulkParams,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String?,
	): SimpleResponse {
		apiCaller.renameBulk(params, token = token)
		return SimpleResponse()
	}

	@PostMapping("/move-bulk")
	fun moveBulk(
		@RequestBody params: MoveBulkParams,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String?,
	): SimpleResponse {
		apiCaller.moveBulk(params, token = token)
		return SimpleResponse()
	}

	@PostMapping("/new-folder")
	fun newFolder(
		@RequestBody params: PathParam,
		@CookieValue(WebConstants.TOKEN_COOKIE_NAME) token: String?,
	): SimpleResponse {
		apiCaller.mkdir(params.path, token = token)
		return SimpleResponse()
	}
}
