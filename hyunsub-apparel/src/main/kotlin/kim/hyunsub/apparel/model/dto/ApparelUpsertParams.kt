package kim.hyunsub.apparel.model.dto

import kim.hyunsub.apparel.model.RestApiApparel

data class ApparelUpsertParams(
	val apparel: RestApiApparel,
	val deletes: List<String>,
	val uploads: List<ApparelUpsertImageParams>,
)

data class ApparelUpsertImageParams(
	val nonce: String,
	val ext: String,
)
