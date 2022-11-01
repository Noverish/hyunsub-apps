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
@RequestMapping("/api/v1/records")
class RecordController(
	private val recordService: RecordService,
) {
	val log = KotlinLogging.logger { }

	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@RequestBody params: RestApiRecord,
	): RestApiRecord {
		log.debug { "[Record Create] params=$params" }

		return recordService.create(params, userAuth.idNo)
	}

	@PutMapping("/{recordId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable recordId: String,
		@RequestBody params: RestApiRecord,
	): RestApiRecord {
		log.debug { "[Record Update] recordId=$recordId, params=$params" }

		if (recordId != params.id) {
			throw ErrorCodeException(ErrorCode.INVALID_PARAMETER, "Record id is different")
		}

		return recordService.update(params, userAuth.idNo)
	}

	@GetMapping("/{recordId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable recordId: String,
	): RestApiRecord {
		log.debug { "[Record Detail] recordId=$recordId" }
		return recordService.detail(
			recordId = recordId,
			userId = userAuth.idNo,
		)
	}

	@DeleteMapping("/{recordId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable recordId: String,
	): SimpleResponse {
		log.debug { "[Record Delete] recordId=$recordId" }
		recordService.delete(
			recordId = recordId,
			userId = userAuth.idNo,
		)
		return SimpleResponse()
	}
}
