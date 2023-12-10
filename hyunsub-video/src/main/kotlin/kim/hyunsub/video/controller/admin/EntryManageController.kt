package kim.hyunsub.video.controller.admin

import kim.hyunsub.common.fs.model.FsRenameBulkData
import kim.hyunsub.common.web.annotation.Authorized
import kim.hyunsub.video.bo.EntryScanBo
import kim.hyunsub.video.model.dto.EntryMoveParams
import kim.hyunsub.video.model.dto.EntryRenameParams
import kim.hyunsub.video.model.dto.EntryScanResult
import kim.hyunsub.video.service.EntryMoveService
import kim.hyunsub.video.service.EntryRenameService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Authorized(["admin"])
@RestController
@RequestMapping("/api/v1/entries/{entryId}/manage")
class EntryManageController(
	private val entryScanBo: EntryScanBo,
	private val entryRenameService: EntryRenameService,
	private val entryMoveService: EntryMoveService,
) {
	private val log = KotlinLogging.logger { }

	@PostMapping("/scan")
	fun scan(
		@PathVariable entryId: String,
	): List<EntryScanResult> {
		log.debug { "[Entry Scan] entryId=$entryId" }
		return entryScanBo.scan(entryId)
	}

	@PostMapping("/rename")
	fun rename(
		@PathVariable entryId: String,
		@RequestBody params: EntryRenameParams,
	): List<FsRenameBulkData> {
		log.debug { "[Entry Rename] entryId=$entryId, params=$params" }
		return entryRenameService.rename(entryId, params)
	}

	@PostMapping("/move")
	fun move(
		@PathVariable entryId: String,
		@RequestBody params: EntryMoveParams,
	): String {
		log.debug { "[Entry Move] entryId=$entryId, params=$params" }
		entryMoveService.move(entryId, params)
		return "success"
	}
}
