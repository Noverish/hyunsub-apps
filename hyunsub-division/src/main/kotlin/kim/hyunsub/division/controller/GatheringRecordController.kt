package kim.hyunsub.division.controller

import kim.hyunsub.common.web.error.ErrorCode
import kim.hyunsub.common.web.error.ErrorCodeException
import kim.hyunsub.common.web.model.SimpleResponse
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.division.model.dto.RestApiRecord
import kim.hyunsub.division.service.RecordService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/gatherings/{gatheringId}/records")
class GatheringRecordController(
	private val recordService: RecordService,
) {
	val log = KotlinLogging.logger { }

	@GetMapping("")
	fun list(@PathVariable gatheringId: String): List<RestApiRecord> {
		return recordService.list(gatheringId)
	}

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@PathVariable gatheringId: String,
		@RequestBody params: RestApiRecord,
	): RestApiRecord {
		log.debug { "[Record Create] gatheringId=$gatheringId, params=$params" }

		if (gatheringId != params.gatheringId) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Gathering id is different")
		}

		return recordService.create(params, userAuth.idNo)
	}

	@PutMapping("/{recordId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable gatheringId: String,
		@PathVariable recordId: String,
		@RequestBody params: RestApiRecord,
	): RestApiRecord {
		log.debug { "[Record Update] gatheringId=$gatheringId, recordId=$recordId, params=$params" }

		if (gatheringId != params.gatheringId) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Gathering id is different")
		}

		if (recordId != params.id) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Record id is different")
		}

		return recordService.update(params, userAuth.idNo)
	}

	@GetMapping("/{recordId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable gatheringId: String,
		@PathVariable recordId: String,
	): RestApiRecord {
		log.debug { "[Record Detail] gatheringId=$gatheringId, recordId=$recordId" }
		return recordService.detail(
			recordId = recordId,
			userId = userAuth.idNo,
			gatheringId = gatheringId,
		)
	}

	@DeleteMapping("/{recordId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable gatheringId: String,
		@PathVariable recordId: String,
	): SimpleResponse {
		log.debug { "[Record Delete] gatheringId=$gatheringId, recordId=$recordId" }
		recordService.delete(
			recordId = recordId,
			userId = userAuth.idNo,
			gatheringId = gatheringId,
		)
		return SimpleResponse()
	}
}
