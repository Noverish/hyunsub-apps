package kim.hyunsub.dutch.controller

import kim.hyunsub.common.model.ApiPageResult
import kim.hyunsub.common.web.model.UserAuth
import kim.hyunsub.dutch.bo.DutchRecordSearchBo
import kim.hyunsub.dutch.model.api.ApiDutchRecordDetail
import kim.hyunsub.dutch.model.api.ApiDutchRecordPreview
import kim.hyunsub.dutch.model.dto.DutchSearchParams
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DutchRecordSearchController(
	private val dutchRecordSearchBo: DutchRecordSearchBo,
) {
	@PostMapping("/api/v1/search/records")
	fun search(
		userAuth: UserAuth,
		@RequestBody params: DutchSearchParams,
	): ApiPageResult<ApiDutchRecordPreview> {
		return dutchRecordSearchBo.search(params)
	}

	@GetMapping("/api/v1/trips/{tripId}/records/{recordId}")
	fun detail(
		userAuth: UserAuth,
		@PathVariable tripId: String,
		@PathVariable recordId: String,
	): ApiDutchRecordDetail? {
		return dutchRecordSearchBo.detail(tripId, recordId)
	}
}
