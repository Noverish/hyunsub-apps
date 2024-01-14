package kim.hyunsub.dutch.controller

import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.bo.DutchRecordMutateBo
import kim.hyunsub.dutch.model.api.ApiDutchRecordDetail
import kim.hyunsub.dutch.model.api.ApiDutchRecordPreview
import kim.hyunsub.dutch.model.dto.DutchRecordParams
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
		@RequestBody params: DutchRecordParams,
	): ApiDutchRecordDetail {
		return dutchRecordMutateBo.create(tripId, params)
	}

	@PutMapping("/{recordId}")
	fun update(
		userAuth: UserAuth,
		@PathVariable tripId: String,
		@PathVariable recordId: String,
		@RequestBody params: DutchRecordParams,
	): ApiDutchRecordDetail {
		return dutchRecordMutateBo.update(tripId, recordId, params)
	}

	@DeleteMapping("/{recordId}")
	fun delete(
		userAuth: UserAuth,
		@PathVariable tripId: String,
		@PathVariable recordId: String,
	): ApiDutchRecordPreview {
		return dutchRecordMutateBo.delete(tripId, recordId)
	}
}
