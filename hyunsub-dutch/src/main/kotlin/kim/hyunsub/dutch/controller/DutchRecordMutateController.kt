package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.bo.DutchRecordMutateBo
import kim.hyunsub.dutch.model.api.ApiDutchRecord
import kim.hyunsub.dutch.model.dto.DutchRecordCreateParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/trips/{tripId}/records")
class DutchRecordMutateController(
	private val dutchRecordMutateBo: DutchRecordMutateBo,
) {
	@PostMapping("")
	fun create(
		userAuth: UserAuth,
		@PathVariable tripId: String,
		@RequestBody params: DutchRecordCreateParams,
	): ApiDutchRecord {
		return dutchRecordMutateBo.create(tripId, params)
	}

	@DeleteMapping("/{recordId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable tripId: String,
		@PathVariable recordId: String,
	): ApiDutchRecord {
		return dutchRecordMutateBo.delete(tripId, recordId)
	}
}
